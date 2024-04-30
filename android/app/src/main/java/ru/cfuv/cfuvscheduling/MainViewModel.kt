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
import ru.cfuv.cfuvscheduling.api.LoginBody
import ru.cfuv.cfuvscheduling.api.NetErrors
import ru.cfuv.cfuvscheduling.api.NetStatus
import ru.cfuv.cfuvscheduling.api.SchedApi
import ru.cfuv.cfuvscheduling.api.TTClassModel
import ru.cfuv.cfuvscheduling.api.UserModel
import java.net.ConnectException
import java.time.LocalDate

val USER_TOKEN_DS_KEY = stringPreferencesKey("token")

class MainViewModel(private val datastore: DataStore<Preferences>) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private var userToken: String? = null

    private val _netStatus = MutableStateFlow(NetStatus(true))
    private val _appBarTitle = MutableStateFlow("")
    private val _currentGroupName = MutableStateFlow("")
    private val _currentGroupIdx = MutableStateFlow(0)
    private val _groupList = MutableStateFlow(listOf<String>())
    private val _currentClasses = MutableStateFlow(listOf<TTClassModel>())
    private val _userData = MutableStateFlow<UserModel?>(null)

    val netStatus: StateFlow<NetStatus>
        get() = _netStatus
    val appBarTitle: StateFlow<String>
        get() = _appBarTitle
    val groupList: StateFlow<List<String>>
        get() = _groupList
    val currentGroupName: StateFlow<String>
        get() = _currentGroupName
    val currentGroupIdx: StateFlow<Int>
        get() = _currentGroupIdx
    val currentClasses: StateFlow<List<TTClassModel>>
        get() = _currentClasses
    val userData: StateFlow<UserModel?>
        get() = _userData

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
        if (resp.code() != 200) {
            Log.e(TAG, "Request to ${resp.raw().request.url} failed!\n" +
                    "Code ${resp.code()}\nResponse: ${resp.body()}")
        }
        return if (resp.code() in 400..499) {
            _netStatus.value = NetStatus(
                false,
                when (resp.code()) {
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

    private suspend fun fetchTimetable() {
        val res = processResp { SchedApi.timetable.getClasses(
            groupName = _currentGroupName.value,
            startDate = LocalDate.now(),
            endDate = LocalDate.now()
        ) } ?: return
        _currentClasses.value = res
    }

    init {
        viewModelScope.launch {
            // Try to fetch user info
            val userTokenFlow = datastore.data.map { it[USER_TOKEN_DS_KEY] }
            userToken = userTokenFlow.first()
            if (userToken != null) {
                val res = processResp { SchedApi.auth.getCurrentUser(userToken!!) } ?: return@launch
                _userData.value = res
            }
        }
        viewModelScope.launch {
            // Fetch groups
            val res = processResp { SchedApi.admin.getGroups() } ?: return@launch
            val strList = mutableListOf<String>()
            res.forEach { strList.add(it.name) }
            _groupList.value = strList.toList()
            _currentGroupName.value = _groupList.value[0]

            // Fetch timetable for selected group
            fetchTimetable()
        }
    }

    fun setAppBarTitle(value: String) {
        _appBarTitle.value = value
    }

    fun setCurrentGroup(idx: Int) {
        _currentGroupIdx.value = idx
        _currentGroupName.value = _groupList.value[idx]
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
        }
    }
    fun registerUser(username: String, pass: String) {
        viewModelScope.launch {
            val res = processResp { SchedApi.auth.registerUser(LoginBody(username, pass)) } ?: return@launch
            // Write token
            datastore.edit { it[USER_TOKEN_DS_KEY] = res.token }
            userToken = res.token
            _userData.value = res.user
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
            currentClasses.value.find { it.id == id }?.comment = comment
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
