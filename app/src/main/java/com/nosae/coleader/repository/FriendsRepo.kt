package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/5/11
 */
class FriendsRepo: BaseRepo() {

    private val userService = RetrofitHelper.userService
    private val teamService = RetrofitHelper.teamService

    suspend fun getFriends() = tryBlock {
        userService.getFriends()
    }

    suspend fun getTeams() = tryBlock {
        teamService.getTeams(1)
    }
}