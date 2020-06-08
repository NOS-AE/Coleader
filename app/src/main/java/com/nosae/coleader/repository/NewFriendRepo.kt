package com.nosae.coleader.repository

import androidx.lifecycle.liveData
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.SearchTeamResDto

/**
 * Create by NOSAE on 2020/5/11
 */
class NewFriendRepo: BaseRepo() {

    private val teamService = RetrofitHelper.teamService
    private val userService = RetrofitHelper.userService

    suspend fun getTeams(keyword: String, page: Int = 1) = tryBlock { teamService.searchTeams(page, keyword) }

    suspend fun getUsers(keyword: String, page: Int = 1) = tryBlock { userService.searchUsers(page, keyword) }
}