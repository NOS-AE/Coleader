package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.data.Task
import com.nosae.coleader.repository.TaskRepo
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by NOSAE on 2020/6/10
 */
class TaskListViewModel(
    private val repo: TaskRepo
): BaseViewModel() {

    private val _tasksRes = MutableLiveData<List<Task>?>()
    val tasksRes: LiveData<List<Task>?> = _tasksRes

    var teamId = -1L
    val isAdmin = MutableLiveData(false)

    fun getTask() = launch {
        val res = if (teamId != -1L) {
            repo.getUserTask()
        } else {
            repo.getTeamTask(teamId = teamId)
        }
        if (res != null && res.errno == "0") {
            _tasksRes.postValue(res.data.rows)
        } else {
            _tasksRes.postValue(null)
        }
    }

    class Factory: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TaskListViewModel(TaskRepo()) as T
        }
    }
}