package ru.cfuv.cfuvscheduling

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val GROUPS = listOf("ПИ-231(1)", "ПИ-231(2)", "ПИ-232(1)", "ПИ-232(2)", "ПИ-233(1)", "ПИ-233(2)")

class MainViewModel : ViewModel() {
    private val _appBarTitle = MutableStateFlow("")
    private val _currentGroupName = MutableStateFlow(GROUPS[0])
    private val _currentGroupIdx = MutableStateFlow(0)
    private val _groupList = MutableStateFlow(GROUPS)

    val appBarTitle: StateFlow<String>
        get() = _appBarTitle
    val groupList: StateFlow<List<String>>
        get() = _groupList
    val currentGroupName: StateFlow<String>
        get() = _currentGroupName
    val currentGroupIdx: StateFlow<Int>
        get() = _currentGroupIdx

    fun setAppBarTitle(value: String) {
        _appBarTitle.value = value
    }

    fun setCurrentGroup(idx: Int) {
        _currentGroupIdx.value = idx
        _currentGroupName.value = _groupList.value[idx]
    }
}