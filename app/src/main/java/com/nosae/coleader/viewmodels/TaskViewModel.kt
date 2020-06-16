package com.nosae.coleader.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.repository.TaskRepo

/**
 * Create by NOSAE on 2020/6/16
 */

class TaskViewModel(private val repo: TaskRepo) : BaseViewModel() {



    class Factory: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TaskViewModel(TaskRepo()) as T
        }
    }
}