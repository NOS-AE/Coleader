package com.nosae.coleader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.MyApplication
import com.nosae.coleader.adapters.MessageItem
import com.nosae.coleader.data.Friend
import com.nosae.coleader.data.TeamResultMessage
import com.nosae.coleader.data.TempData
import com.nosae.coleader.data.UserResultMessage
import com.nosae.coleader.repository.MessageRepo
import com.nosae.coleader.utils.add
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.postAddAll

/**
 * Create by NOSAE on 2020/6/17
 */
class MessageViewModel(private val repo: MessageRepo): BaseViewModel() {

    var id = -1L
    var name = ""
    var type = 0
    private val socket = MyApplication.socket

    val msgRes = MutableLiveData<ArrayList<MessageItem>>(arrayListOf())

    fun send(msg: String) {
        val message = MessageItem(msg, "", "", false)
        if (type == 0) {
            debug("send-msg $msg $id")
            socket.emit("send-msg", msg, "text", id)
        } else {
            debug("send-team-msg $msg $id")
            socket.emit("send-team-msg", msg, "text", id)
        }
        msgRes.add(message, 0)
    }

    fun receiveUserMsg(msg: UserResultMessage) {
        val message = MessageItem(msg.chatRecord, msg.userInfo.avatar, msg.userInfo.nickname, true)
        msgRes.add(message, 0)
    }

    fun receiveTeamMsg(msg: TeamResultMessage) {
        val message = MessageItem(msg.chatRecord, msg.userInfo.avatar, msg.userInfo.nickname, true)
        msgRes.add(message, 0)
    }

    private val userMap = mutableMapOf<Long, Friend>()
    fun getMessage() = launchNetwork {
        if (type == 0) {
            val res = repo.getUserMessage(id)
            val userInfo = repo.getUserInfo(id)
            if (res != null && res.errno == "0" && userInfo != null && userInfo.errno == "0") {
                val list = ArrayList<MessageItem>()
                res.data[0].records.rows.forEach {
                    list += if (TempData.userInfo!!.data.id != it.userId) {
                        MessageItem(
                            it.chatRecord,
                            userInfo.data.avatar,
                            userInfo.data.nickname,
                            true
                        )
                    }
                    else {
                        MessageItem(
                            it.chatRecord,
                            TempData.userInfo!!.data.avatar,
                            TempData.userInfo!!.data.nickname,
                            false
                        )
                    }
                }
                msgRes.postAddAll(list.reversed())
            }
        } else {
            val res = repo.getTeamMessage(id)
            val teamInfo = repo.getTeamInfo(id)
            if (res != null && res.errno == "0" && teamInfo != null && teamInfo.errno == "0") {
                val list = ArrayList<MessageItem>()
                for (i in res.data.rows) {
                    list += if (i.userId != TempData.userInfo!!.data.id) {
                        if (userMap[i.userId] == null) {
                            val userRes = repo.getUserInfo(i.userId)
                            if (userRes != null && userRes.errno == "0") {
                                userMap[i.userId] = userRes.data
                            }
                        }
                        val user = userMap[i.userId] ?: continue
                        MessageItem(
                            i.chatRecord,
                            user.avatar,
                            user.nickname,
                            true
                        )
                    } else
                        MessageItem(i.chatRecord, TempData.userInfo!!.data.avatar, TempData.userInfo!!.data.nickname, false)
                }
                msgRes.postAddAll(list.reversed())
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MessageViewModel(MessageRepo()) as T
        }
    }
}