package com.nosae.coleader.repository

import androidx.lifecycle.liveData
import com.nosae.coleader.data.*
import com.nosae.coleader.utils.debug
import kotlinx.coroutines.Dispatchers

/**
 * Create by NOSAE on 2020/5/7
 */
class UserInfoRepo: BaseRepo() {

    private val userService = RetrofitHelper.userService
    private val teamService = RetrofitHelper.teamService

    // suspend fun getUserInfo() = tryBlock {
    //     userService.getUserInfo().also {
    //         debug(it.toString())
    //     }
    // }

    // suspend fun getTeams() = tryBlock {
    //     // TODO 分页
    //     teamService.getTeams(1).also {
    //         debug(it.toString())
    //     }
    // }

    suspend fun getUserInfo(id: Long) = tryBlock {
        userService.getUserInfo(id.toString())
            ?.data
    }

    suspend fun getTeams(id: Long) = tryBlock {
        teamService.getTeams(1, id)
            .data.rows
    }

    suspend fun sendRequest(id: Long, msg: String) = tryBlock {
        val dto = FriendRequestDto(id, msg)
        userService.sendFriendRequest(dto)
    }
}