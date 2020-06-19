package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.data.Form
import com.nosae.coleader.repository.FormRepo

/**
 * Create by NOSAE on 2020/6/18
 */
class FormListViewModel(private val repo: FormRepo): BaseViewModel() {

    private val _formListRes = MutableLiveData<List<Form>?>()
    val formListRes: LiveData<List<Form>?> = _formListRes

    var teamId = -1L
    val isAdmin = MutableLiveData(false)

    fun getForms() = launchNetwork {
        val res = repo.getForms(teamId)
        if (res != null && res.errno == "0") {
            _formListRes.postValue(res.data.rows)
        } else {
            _formListRes.postValue(null)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FormListViewModel(FormRepo()) as T
        }
    }
}