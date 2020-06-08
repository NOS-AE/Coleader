package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/5/23
 */
class TeamRepo: BaseRepo() {

    private val teamService = RetrofitHelper.teamService

    suspend fun getTeamInfo(id: Long) = tryBlock {
        teamService.getTeamInfo(id)
    }

    suspend fun getTeamMembers(id: Long) = tryBlock {
        teamService.getMembers(id)
    }
}