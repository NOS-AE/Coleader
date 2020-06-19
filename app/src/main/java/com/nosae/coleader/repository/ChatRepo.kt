package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/6/19
 */
class ChatRepo: BaseRepo() {
    private val punchService = RetrofitHelper.punchService
    private val userService = RetrofitHelper.userService
    private val teamService = RetrofitHelper.teamService

    suspend fun getUserPunch(page: Int = 1) = tryBlock {
        punchService.getPunches(page)
    }

    suspend fun getLastUserMessage() = tryBlock {
        userService.getLastMessage()
    }

    suspend fun getLastTeamMessage() = tryBlock {
        teamService.getLastMessage()
    }

    suspend fun getUserInfo(id: Long) = tryBlock {
        userService.getUserInfo(id.toString())
    }

    suspend fun getTeamInfo(id: Long) = tryBlock {
        teamService.getTeamInfo(id)
    }
}