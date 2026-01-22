package com.abhi.inc.tasknote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<ApiTask>>()
    val tasks: LiveData<List<ApiTask>> get() = _tasks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getTasks().take(20)
                _tasks.value = response
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}