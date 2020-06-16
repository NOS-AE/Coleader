package com.nosae.coleader.repository

import com.nosae.coleader.data.CreateTaskDto
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.UpdateTaskDto

/**
 * Create by NOSAE on 2020/6/10
 */
class TaskRepo: BaseRepo() {
    private val taskService = RetrofitHelper.taskService

    suspend fun getUserTask(page: Int = 1) = tryBlock {
        taskService.getTasks(page)
    }

    suspend fun getTeamTask(page: Int = 1, teamId: Long) = tryBlock {
        taskService.getTasks(page, teamId = teamId)
    }

    suspend fun createTask(
        teamId: Long,
        intro: String,
        content: String,
        remark: String,
        startAt: String,
        endAt: String,
        participants: List<Long>
    ) = tryBlock {
        val dto = CreateTaskDto(teamId, startAt, endAt, intro, remark, content, participants)
        taskService.createTask(dto)
    }

    suspend fun updateTask(
        teamId: Long,
        intro: String,
        content: String,
        remark: String,
        startAt: String,
        endAt: String,
        participants: List<Long>,
        taskId: Long
    ) = tryBlock {
        val dto = UpdateTaskDto(teamId, startAt, endAt, intro, remark, content, participants, taskId)
        taskService.updateTask(dto)
    }

    suspend fun deleteTask(taskId: Long) = tryBlock {
        taskService.deleteTask(taskId)
    }
}