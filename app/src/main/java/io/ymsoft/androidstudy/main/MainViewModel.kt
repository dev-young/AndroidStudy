package io.ymsoft.androidstudy.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    val dday = MutableLiveData<String>()
    val repo = DateImpl()

    fun cal(){
        viewModelScope.launch {
            val current = Date().time / 86400000
            val d = repo.get().time / 86400000
            val _dday = d - current
            dday.value = _dday.toString()
        }
    }

}