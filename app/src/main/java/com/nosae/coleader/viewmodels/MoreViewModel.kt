package com.nosae.coleader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.nosae.coleader.data.TempData
import com.nosae.coleader.data.UserInfoResDto
import com.nosae.coleader.repository.MoreRepo

/**
 * Create by NOSAE on 2020/5/7
 */
class MoreViewModel(
    private val repo: MoreRepo
): BaseViewModel() {

    private val _userInfo = MutableLiveData<UserInfoResDto?>()
    val userInfo = _userInfo.map {
        it?.data
    }

    init {
        getInfo()
    }


    fun getInfo() = launchNetwork {
        if (TempData.userInfo == null) {
            TempData.userInfo = repo.getUserInfo()
        }
        _userInfo.postValue(TempData.userInfo)
    }

    val avatar = _userInfo.map {
        it?.data?.avatar
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MoreViewModel(MoreRepo()) as T
        }
    }
}