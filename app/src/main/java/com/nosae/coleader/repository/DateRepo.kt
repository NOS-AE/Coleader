package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/5/31
 */
class DateRepo: BaseRepo() {
    private val dateService = RetrofitHelper.dateService

    suspend fun getTimeDate(date: String, page: Int = 1) = tryBlock {
        dateService.getDate(page, date)
    }

    suspend fun getTeamDate(teamId: Long, date: String, page: Int = 1) = tryBlock {
        dateService.getDate(page, date, teamId)
    }
}