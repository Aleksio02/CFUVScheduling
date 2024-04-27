package ru.cfuv.cfuvscheduling

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import ru.cfuv.cfuvscheduling.api.NetErrors
import ru.cfuv.cfuvscheduling.api.NetStatus
import ru.cfuv.cfuvscheduling.api.SchedApi
import java.net.ConnectException

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _netStatus = MutableStateFlow(NetStatus(true))
    private val _appBarTitle = MutableStateFlow("")
    private val _currentGroupName = MutableStateFlow("")
    private val _currentGroupIdx = MutableStateFlow(0)
    private val _groupList = MutableStateFlow(listOf<String>())

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

    private suspend fun <T> processResp(func: suspend () -> Response<T>): T? {
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
            _netStatus.value = NetStatus(false, NetErrors.UNKNOWN)
            null
        } else if (resp.code() in 500 .. 599 || resp.body() == null) {
            _netStatus.value = NetStatus(false, NetErrors.SERVERSIDE)
            null
        } else resp.body()
    }

    init {
        // Fetch groups
        viewModelScope.launch {
            val res = processResp { SchedApi.admin.getGroups() } ?: return@launch
            val strList = mutableListOf<String>()
            res.forEach { strList.add(it.name) }
            _groupList.value = strList.toList()
            _currentGroupName.value = _groupList.value[0]
        }
    }

    fun setAppBarTitle(value: String) {
        _appBarTitle.value = value
    }

    fun setCurrentGroup(idx: Int) {
        _currentGroupIdx.value = idx
        _currentGroupName.value = _groupList.value[idx]
    }
}