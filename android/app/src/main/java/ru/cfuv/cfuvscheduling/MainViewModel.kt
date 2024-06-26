package ru.cfuv.cfuvscheduling

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import ru.cfuv.cfuvscheduling.api.ClassCreationBody
import ru.cfuv.cfuvscheduling.api.ClassTypeModel
import ru.cfuv.cfuvscheduling.api.GroupModel
import ru.cfuv.cfuvscheduling.api.LoginBody
import ru.cfuv.cfuvscheduling.api.NetErrors
import ru.cfuv.cfuvscheduling.api.NetStatus
import ru.cfuv.cfuvscheduling.api.SchedApi
import ru.cfuv.cfuvscheduling.api.TTClassModel
import ru.cfuv.cfuvscheduling.api.UserModel
import java.net.ConnectException
import java.time.LocalDate

val USER_TOKEN_DS_KEY = stringPreferencesKey("token")
enum class ClassTypes {
    LECTURE, PRACTICAL, CONSULTATION, UNKNOWN
}

enum class UserRoles {
    TEACHER, ADMIN
}

class MainViewModel(private val datastore: DataStore<Preferences>) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private var userToken: String? = null

    private val _netStatus = MutableStateFlow(NetStatus(true))
    private val _appBarTitle = MutableStateFlow("")
    private val _currentGroup = MutableStateFlow(GroupModel(0, ""))
    private val _currentGroupIdx = MutableStateFlow(0)
    private val _groupList = MutableStateFlow(listOf<GroupModel>())
    private val _currentClasses = MutableStateFlow(listOf<TTClassModel>())
    private val _userData = MutableStateFlow<UserModel?>(null)
    private val _userCanCreateClasses = MutableStateFlow(false)
    private val _currentDate = MutableStateFlow(LocalDate.now())
    private val _classTypes = MutableStateFlow(listOf<ClassTypeModel>())
    private val _teachers = MutableStateFlow(listOf<UserModel>())

    val netStatus: StateFlow<NetStatus>
        get() = _netStatus
    val appBarTitle: StateFlow<String>
        get() = _appBarTitle
    val groupList: StateFlow<List<GroupModel>>
        get() = _groupList
    val currentGroup: StateFlow<GroupModel>
        get() = _currentGroup
    val currentGroupIdx: StateFlow<Int>
        get() = _currentGroupIdx
    val currentClasses: StateFlow<List<TTClassModel>>
        get() = _currentClasses
    val userData: StateFlow<UserModel?>
        get() = _userData
    val userCanCreateClasses: StateFlow<Boolean>
        get() = _userCanCreateClasses
    val currentDate: StateFlow<LocalDate>
        get() = _currentDate
    val classTypes: StateFlow<List<ClassTypeModel>>
        get() = _classTypes
    val teachers: StateFlow<List<UserModel>>
        get() = _teachers

    private suspend fun <T> processResp(func: suspend () -> Response<T>): T? {
        _netStatus.value = NetStatus(true)
        val resp: Response<T>
        try {
            resp = func()
            Log.d(TAG, "Performed request to ${resp.raw().request.url}")
        } catch (e: ConnectException) {
            Log.e(TAG, "Error performing request!\n${e.message}")
            _netStatus.value = NetStatus(false, NetErrors.NO_INTERNET)
            return null
        } catch (e: IOException) {
            Log.e(TAG, "Error performing request!\n${e.message}")
            _netStatus.value = NetStatus(false, NetErrors.TIMEOUT)
            return null
        }
        val errorBody = resp.errorBody()?.string()
        if (resp.code() != 200) {
            Log.e(TAG, "Request to ${resp.raw().request.url} failed!\n" +
                    "Code ${resp.code()}\nResponse: $errorBody")
        }
        return if (resp.code() in 400..499) {
            _netStatus.value = NetStatus(
                false,
                when (resp.code()) {
                    400 -> NetErrors.BAD_REQUEST
                    401 -> NetErrors.UNAUTHORIZED
                    else -> NetErrors.UNKNOWN
                }
            )
            null
        } else if (resp.code() in 500 .. 599) {
            _netStatus.value = NetStatus(false, NetErrors.SERVERSIDE)
            null
        } else resp.body()
    }

    private suspend fun fetchTimetable(date: LocalDate = LocalDate.now()) {
        val res = processResp { SchedApi.timetable.getClasses(
            groupName = _currentGroup.value.name,
            startDate = date,
            endDate = date
        ) } ?: return
        _currentClasses.value = res
        _currentDate.value = date
    }

    private suspend fun doUserRelatedFetches(user: UserModel) {
        if (user.role == UserRoles.ADMIN.name) {
            // Fetch teachers
            val teachersRes = processResp { SchedApi.auth.getAllTeachers() } ?: return
            _teachers.value = teachersRes

            // Fetch class types
            val typesRes = processResp { SchedApi.admin.getClassTypes() } ?: return
            _classTypes.value = typesRes
        }
    }

    init {
        viewModelScope.launch {
            // Try to fetch user info
            val userTokenFlow = datastore.data.map { it[USER_TOKEN_DS_KEY] }
            userToken = userTokenFlow.first()
            if (userToken != null) {
                val res = processResp { SchedApi.auth.getCurrentUser(userToken!!) } ?: return@launch
                _userData.value = res
                _userCanCreateClasses.value = res.role == UserRoles.TEACHER.name || res.role == UserRoles.ADMIN.name
                doUserRelatedFetches(res)
            }
        }
        viewModelScope.launch {
            // Fetch groups
            val res = processResp { SchedApi.admin.getGroups() } ?: return@launch
            _groupList.value = res
            _currentGroup.value = _groupList.value[0]

            // Fetch timetable for selected group
            fetchTimetable()
        }
    }

    fun setAppBarTitle(value: String) {
        _appBarTitle.value = value
    }

    fun setCurrentGroup(idx: Int) {
        _currentGroupIdx.value = idx
        _currentGroup.value = _groupList.value[idx]
        // Fetch timetable for selected group
        viewModelScope.launch { fetchTimetable() }
    }

    // Authorization
    fun loginUser(username: String, pass: String) {
        viewModelScope.launch {
            val res = processResp { SchedApi.auth.authenticateUser(LoginBody(username, pass)) } ?: return@launch
            // Write token
            datastore.edit { it[USER_TOKEN_DS_KEY] = res.token }
            userToken = res.token
            _userData.value = res.user
            doUserRelatedFetches(res.user)
        }
    }
    fun registerUser(username: String, pass: String) {
        viewModelScope.launch {
            val res = processResp { SchedApi.auth.registerUser(LoginBody(username, pass)) } ?: return@launch
            // Write token
            datastore.edit { it[USER_TOKEN_DS_KEY] = res.token }
            userToken = res.token
            _userData.value = res.user
            doUserRelatedFetches(res.user)
        }
    }
    fun logoutUser() {
        viewModelScope.launch {
            datastore.edit { it.remove(USER_TOKEN_DS_KEY) }
        }
        _userData.value = null
    }

    // Timetable
    fun updateClassComment(id: Int, comment: String) {
        if (userToken == null) {
            return
        }
        viewModelScope.launch {
            processResp { SchedApi.timetable.addComment(userToken!!, id, comment) }
            val mutableClasses = currentClasses.value.toMutableList()
            mutableClasses.replaceAll { if (it.id == id) it.copy(comment = comment) else it }
            _currentClasses.value = mutableClasses.toList()
        }
    }
    // Break UDF a little by calling a callback on success
    fun createConsultation(name: String, room: String, position: Int, comment: String, date: LocalDate, onSuccess: () -> Unit) {
        if (userToken == null) {
            return
        }
        viewModelScope.launch {
            processResp { SchedApi.timetable.createConsultation(
                token = userToken!!,
                body = ClassCreationBody(
                    subjectName = name,
                    classroom = room,
                    duration = ClassCreationBody.N(position),
                    comment = comment,
                    group = ClassCreationBody.ID(_currentGroup.value.id),
                    classDate = date
                )
            ) }
            // Re-fetch timetable
            fetchTimetable()
            onSuccess()
        }
    }
    fun createClass(name: String, typeID: Int, room: String, position: Int, comment: String, date: LocalDate, teacherID: Int, onSuccess: () -> Unit) {
        if (userToken == null) {
            return
        }
        viewModelScope.launch {
            processResp { SchedApi.timetable.createClass(
                token = userToken!!,
                body = ClassCreationBody(
                    subjectName = name,
                    classroom = room,
                    duration = ClassCreationBody.N(position),
                    comment = comment,
                    group = ClassCreationBody.ID(_currentGroup.value.id),
                    classType = ClassCreationBody.ID(typeID),
                    classDate = date,
                    teacher = ClassCreationBody.ID(teacherID)
                )
            ) }
            // Re-fetch timetable
            fetchTimetable()
            onSuccess()
        }
    }
    fun deleteClass(id: Int) {
        if (userToken == null) {
            return
        }
        viewModelScope.launch {
            processResp { SchedApi.timetable.deleteClass(userToken!!, id) }
            // Re-fetch timetable
            fetchTimetable()
        }
    }
    fun changeDate(dir: Long) {
        viewModelScope.launch {
            val tempDate = _currentDate.value.plusDays(dir)
            fetchTimetable(tempDate)
        }
    }
}

class MainViewModelFactory(
    private val datastore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(datastore) as T
    }
}
