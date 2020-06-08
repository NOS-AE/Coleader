package com.nosae.coleader.viewmodels

import androidx.lifecycle.*
import com.nosae.coleader.data.*
import com.nosae.coleader.repository.UserInfoRepo

/**
 * Create by NOSAE on 2020/5/7
 */

class UserInfoViewModel(
    private val repo: UserInfoRepo
) : BaseViewModel() {

    private var userInfo = MutableLiveData<Friend?>()

    var id = 0L

    val avatar = userInfo.map {
        it?.avatar
    }
    val nickname = userInfo.map {
        it?.nickname
    }
    private val _teamList = MutableLiveData<List<Team>?>()
    val teamList: LiveData<List<Team>?> = _teamList
    val userInfoList = userInfo.map {
        if (it == null) {
            null
        } else {
            val list = mutableListOf<UserInfo>()
            list += UserInfo("姓名", it.username)
            list += UserInfo("邮箱", it.email)
            list += UserInfo("加入时间", it.createdAt)
            list += UserInfo("更新时间", it.updatedAt)
            list += UserInfo("省份", it.province)
            list += UserInfo("城市", it.city)
            list.toList()
        }
    }

    private val _requestRes = MutableLiveData<String>()
    val requestRes: LiveData<String> = _requestRes

    fun getInfo() {
        getTeams()
        getUserInfo()
    }

    fun sendRequest(msg: String) = launchNetwork {
        val res = repo.sendRequest(id, msg)
        if (res == null || res.errno != "0") {
            _requestRes.postValue("申请发送失败")
        } else {
            _requestRes.postValue("申请发送成功")
        }
    }

    fun getTeams() = launchNetwork {
        val res = repo.getTeams(id)
        _teamList.postValue(res)
    }

    fun getUserInfo() = launchNetwork {
        val res = repo.getUserInfo(id)
        userInfo.postValue(res)

    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserInfoViewModel(UserInfoRepo()) as T
        }
    }
}