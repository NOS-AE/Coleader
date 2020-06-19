package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.adapters.ChatItem
import com.nosae.coleader.repository.ChatRepo

/**
 * Create by NOSAE on 2020/5/8
 */
class ChatViewModel(private val repo: ChatRepo): BaseViewModel() {

    private val _chatRes = MutableLiveData<List<ChatItem>>()
    val chatRes: LiveData<List<ChatItem>> = _chatRes

    init {
        getLastMessage()
    }

    fun getLastMessage() = launchNetwork {
        val teamRes = repo.getLastTeamMessage()
        val userRes = repo.getLastUserMessage()
        if (teamRes == null || teamRes.errno != "0" || userRes == null || userRes.errno != "0") {
            _chatRes.postValue(null)
            return@launchNetwork
        }
        val res = ArrayList<ChatItem>()
        for (i in teamRes.data) {
            if (i.records.rows.isEmpty()) continue
            val record = i.records.rows[0]
            val teamInfo = repo.getTeamInfo(record.teamId)
            if (teamInfo == null || teamInfo.errno != "0") continue
            res += ChatItem(record.teamId, teamInfo.data.teamName, i.records.rows[0].chatRecord, teamInfo.data.avatar, i.count, 1)

        }
        for (i in userRes.data) {
            if (i.records.rows.isEmpty()) continue
            val record = i.records.rows[0]
            val userInfo = repo.getUserInfo(i.toId)
            if (userInfo == null || userInfo.errno != "0") continue
            res += ChatItem(i.toId, userInfo.data.nickname, record.chatRecord, userInfo.data.avatar, i.count, 0)
        }
        _chatRes.postValue(res)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChatViewModel(ChatRepo()) as T
        }
    }
}