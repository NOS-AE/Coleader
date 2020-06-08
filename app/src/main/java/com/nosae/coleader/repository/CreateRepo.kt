package com.nosae.coleader.repository

import com.nosae.coleader.data.CreateTeamDto
import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/5/14
 */
class CreateRepo: BaseRepo() {

    private val teamService = RetrofitHelper.teamService

    suspend fun submit(name: String, intro: String) = tryBlock {
        val dto = CreateTeamDto(name, intro)
        teamService.createTeam(dto)
    }
}