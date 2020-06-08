package com.nosae.coleader.repository

import com.nosae.coleader.data.CreateDateDto
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.UpdateDateDto

/**
 * Create by NOSAE on 2020/5/31
 */
class EditDateRepo: BaseRepo() {
    private val dateService = RetrofitHelper.dateService

    suspend fun createDate(
        teamId: Long,
        startAt: String, // 2020-05-06 10:23:53
        endAt: String, // 2020-05-06 11:22:53
        introduction: String, // maomao
        detail: String, // maomaomaomao
        label: List<String>,
        level: Int // 0
    ) = tryBlock {
        val dto = CreateDateDto(teamId, startAt, endAt, introduction, detail, label, level)
        dateService.createDate(dto)
    }

    suspend fun updateDate(
        teamId: Long,
        startAt: String, // 2020-05-06 10:23:53
        endAt: String, // 2020-05-06 11:22:53
        introduction: String, // maomao
        detail: String, // maomaomaomao
        label: List<String>,
        level: Int, // 0
        scheduleId: Long
    ) = tryBlock {
        val dto = UpdateDateDto(teamId, startAt, endAt, introduction, detail, label, level, scheduleId)
        dateService.updateDate(dto)
    }

    suspend fun deleteDate(teamId: Long, scheduleId: Long) = tryBlock {
        dateService.deleteDate(teamId, scheduleId)
    }
}