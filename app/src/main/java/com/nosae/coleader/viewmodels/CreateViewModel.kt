package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Team
import com.nosae.coleader.data.toTeam
import com.nosae.coleader.repository.CreateRepo

/**
 * Create by NOSAE on 2020/5/14
 */
class CreateViewModel(
    private val repo: CreateRepo
): BaseViewModel() {

    val teamName = MutableLiveData<String>()
    val teamIntro = MutableLiveData<String>()

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String?> = _message

    private val _newTeam = MutableLiveData<Team>()
    val newTeam: LiveData<Team> = _newTeam

    fun checkInput() {
        val name = teamName.value
        val intro = teamIntro.value
        when {
            name.isNullOrBlank() -> _message.value = "团队名称未填写"
            intro.isNullOrBlank() -> _message.value = "团队介绍未填写"
            else -> {
                submit(name, intro)
            }
        }
    }

    private fun submit(name: String, intro: String) {
        launchNetwork {
            val res = repo.submit(name, intro)
            if (res == null || res.errno != "0") {
                _message.postValue("创建团队失败")
            } else {
                _newTeam.postValue(res.data.toTeam())
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CreateViewModel(CreateRepo()) as T
        }
    }
}