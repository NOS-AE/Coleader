package com.nosae.coleader.repository

import com.nosae.coleader.data.CreatePunchDto
import com.nosae.coleader.data.PunchDto
import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/6/6
 */
class PunchRepo: BaseRepo() {
    private val punchService = RetrofitHelper.punchService

    suspend fun createPunch(
        teamId: Long,
        startAt: String,
        endAt: String,
        intro: String,
        participants: List<Long>
    ) = tryBlock {
        val dto = CreatePunchDto(teamId, participants, startAt, endAt, intro)
        punchService.createPunch(dto)
    }

    suspend fun getUserPunch(page: Int = 1) = tryBlock {
        punchService.getPunches(page)
    }

    suspend fun getTeamPunch(page: Int = 1, teamId: Long) = tryBlock {
        punchService.getPunches(page, teamId)
    }

    suspend fun punch(teamId: Long, cardId: Long) = tryBlock {
        val dto = PunchDto(teamId, cardId)
        punchService.punch(dto)
    }
}