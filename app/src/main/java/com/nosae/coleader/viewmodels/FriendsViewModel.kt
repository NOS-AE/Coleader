package com.nosae.coleader.viewmodels

import androidx.lifecycle.*
import com.nosae.coleader.data.Friend
import com.nosae.coleader.data.FriendItem
import com.nosae.coleader.data.Team
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.FriendsRepo

/**
 * Create by NOSAE on 2020/5/11
 */
class FriendsViewModel(
    private val repo: FriendsRepo
): BaseViewModel() {

    private val _friendsList = MutableLiveData<List<Friend>?>()
    val friendsList: LiveData<List<FriendItem>?> = _friendsList.map {
        if (it == null) {
            null
        } else {
            val ret = mutableListOf<FriendItem>()
            it.forEach { bean ->
                ret += FriendItem(bean.id, bean.avatar, bean.nickname, bean.email)
            }
            ret
        }
    }

    private val _teamList = MutableLiveData<List<Team>?>()
    val teamList: LiveData<List<FriendItem>?> = _teamList.map {
        if (it == null) {
            null
        } else {
            val ret = mutableListOf<FriendItem>()
            it.forEach { bean ->
                ret += FriendItem(bean.id, bean.avatar, bean.teamName, bean.introduction)
            }
            ret
        }
    }

    init {
        getFriends()
        getTeams()
    }

    fun getFriends() {
        launchNetwork(noNetWork = {
            _friendsList.value = null
        }) {
            if (TempData.friendList == null) {
                val res = repo.getFriends()
                if (res != null && res.errno == "0") {
                    TempData.friendList = res.data.rows
                } else {
                    TempData.friendList = null
                }
            }
            _friendsList.postValue(TempData.friendList)
        }
    }

    fun getTeams() {
        launchNetwork(noNetWork = {
            _teamList.value = null
        }) {
            if (TempData.teamList == null) {
                val res = repo.getTeams()
                if (res != null && res.errno == "0") {
                    TempData.teamList = res.data.rows
                } else {
                    TempData.teamList = null
                }
            }
            _teamList.postValue(TempData.teamList)
        }
    }

    fun addTeam(team: Team) {
        val old = _teamList.value
        val new = old?.toMutableList()?.apply { add(team) }
            ?: listOf(team)
        _teamList.value = new
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FriendsViewModel(FriendsRepo()) as T
        }
    }
}