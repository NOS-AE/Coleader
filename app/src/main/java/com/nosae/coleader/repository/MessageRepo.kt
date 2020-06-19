package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/6/19
 */
class MessageRepo: BaseRepo() {
    private val teamService = RetrofitHelper.teamService
    private val userService = RetrofitHelper.userService

    suspend fun getUserMessage(id: Long, page: Int = 1) = tryBlock {
        userService.getMessage(page, id)
    }

    suspend fun getTeamMessage(id: Long, page: Int = 1) = tryBlock {
        teamService.getMessage(page, id)
    }

    suspend fun getTeamInfo(id: Long) = tryBlock {
        teamService.getTeamInfo(id)
    }

    suspend fun getUserInfo(id: Long) = tryBlock {
        userService.getUserInfo(id.toString())
    }
}