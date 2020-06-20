package com.nosae.coleader.viewmodels

import androidx.lifecycle.*
import com.nosae.coleader.MyApplication
import com.nosae.coleader.data.*
import com.nosae.coleader.repository.MainRepo
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.postEvent
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Create by NOSAE on 2020/6/7
 */
class MainViewModel(
    private val repo: MainRepo
): BaseViewModel() {

    private val _punchRes = MutableLiveData<List<Punch>>()
    val punchRes: LiveData<List<Punch>> = _punchRes

    init {
        buildWS()
        getLastPunch()
    }

    private fun getLastPunch() = launchNetwork {
        val res = repo.getUserPunch(1)
        if (res != null && res.errno == "0") {
            _punchRes.postValue(res.data.rows)
        } else {
            _punchRes.postValue(null)
        }
    }

    private fun buildWS() {
        debug("buildWs")
        viewModelScope.launch(Dispatchers.IO) {
            val socket = IO.socket("http://yjcxlr.cn:3000?token=${RetrofitHelper.getToken()}")
                .connect()
            MyApplication.socket = socket
            receiveMessage(socket)
            beating()
        }
    }

    private suspend fun beating() {
        while (true) {
            try {
                RetrofitHelper.userService.beat()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(5 * 60 * 1000)
        }
    }

    private fun receiveMessage(socket: Socket) {
        socket.on("get-msg") {
            val json = it[0] as JSONObject
            debug("get-msg $json")
            val info = json.getJSONObject("userInfo")
            val message = UserResultMessage(
                json.getLong("id"),
                json.getLong("toId"),
                json.getString("chatRecord"),
                json.getInt("isRead"),
                json.getString("type"),
                json.getString("updatedAt"),
                json.getString("createdAt"),
                UserResultMessage.Info(
                    info.getLong("id"),
                    info.getString("username"),
                    info.getString("nickname"),
                    if (info.isNull("avatar")) "" else info.getString("avatar")
                )
            )
            postEvent(ReceiveUserMessageEvent(message))
            socket.emit("msg-read", message.id, message.toId)
        }.on("get-team-msg") {
            val json = it[0] as JSONObject
            debug("get-team-msg $json")
            val info = json.getJSONObject("userinfo")
            val message = TeamResultMessage(
                json.getLong("id"),
                json.getString("chatRecord"),
                json.getString("type"),
                json.getLong("teamId"),
                json.getString("updatedAt"),
                json.getString("createdAt"),
                TeamResultMessage.Info(
                    info.getLong("id"),
                    info.getString("username"),
                    info.getString("nickname"),
                    if (info.isNull("avatar")) "" else info.getString("avatar")
                )
            )
            postEvent(ReceiveTeamMessageEvent(message))
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(MainRepo()) as T
        }
    }
}