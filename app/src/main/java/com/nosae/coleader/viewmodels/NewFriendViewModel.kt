package com.nosae.coleader.viewmodels

import androidx.lifecycle.*
import com.nosae.coleader.adapters.NewFriend
import com.nosae.coleader.data.Friend
import com.nosae.coleader.data.SearchTeam
import com.nosae.coleader.data.SearchUser
import com.nosae.coleader.repository.NewFriendRepo

/**
 * Create by NOSAE on 2020/5/11
 */

class NewFriendViewModel(
    private val repo: NewFriendRepo
) : BaseViewModel() {

    private val _userRes = MutableLiveData<List<SearchUser>?>()
    private val _teamRes = MutableLiveData<List<SearchTeam>?>()

    var userList: List<SearchUser>? = null
    var teamList: List<SearchTeam>? = null

    val searchType = MutableLiveData(0)

    val searchRes: LiveData<List<NewFriend>?> = searchType.switchMap {
        return@switchMap if (it == 0) {
            _userRes.map { list ->
                if (list == null)
                    null
                else {
                    val res = mutableListOf<NewFriend>()
                    list.forEach { bean ->
                        res += NewFriend(bean.avatar, "${bean.nickname}(${bean.username})", "${bean.province} ${bean.city}")
                    }
                    res.toList()
                }
            }
        } else {
            _teamRes.map { list ->
                if (list == null)
                    null
                else {
                    val res = mutableListOf<NewFriend>()
                    list.forEach { bean ->
                        res += NewFriend(bean.avatar, bean.teamName, bean.introduction)
                    }
                    res.toList()
                }
            }
        }
    }

    private fun getUsers(kw: String) {
        launchNetwork {
            val res = repo.getUsers(kw)
            if (res == null || res.errno != "0") {
                _userRes.postValue(null)
                return@launchNetwork
            }
            _userRes.postValue(res.data.rows)
            userList = res.data.rows
            isLoading.postValue(false)
        }
    }

    private fun getTeams(kw: String) {
        launchNetwork {
            val res = repo.getTeams(kw)
            if (res == null || res.errno != "0") {
                _teamRes.postValue(null)
                return@launchNetwork
            }
            _teamRes.postValue(res.data.rows)
            teamList = res.data.rows
            isLoading.postValue(false)
        }
    }

    fun search(keyword: String) {
        if (keyword.isBlank()) {
            return
        }
        isLoading.value = true
        if (searchType.value == 0) {
            getUsers(keyword)
        } else if (searchType.value == 1) {
            getTeams(keyword)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NewFriendViewModel(NewFriendRepo()) as T
        }
    }
}