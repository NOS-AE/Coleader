package com.nosae.coleader.repository

import com.nosae.coleader.data.CreateTaskDto
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.SubmitTaskDto
import okhttp3.MultipartBody

/**
 * Create by NOSAE on 2020/6/10
 */
class TaskRepo: BaseRepo() {
    private val taskService = RetrofitHelper.taskService
    private val utilService = RetrofitHelper.utilService

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

    suspend fun deleteTask(taskId: Long) = tryBlock {
        taskService.deleteTask(taskId)
    }

    suspend fun uploadFile(type: String, file: MultipartBody.Part) = tryBlock {
        utilService.uploadFile(type, file)
    }

    suspend fun downloadFile(url: String) = tryBlock {
        utilService.downloadFile(url)
    }

    suspend fun submitTask(
        taskId: Long,
        teamId: Long,
        title: String,
        content: String,
        fileUrls: List<String>,
        fileNames: List<String>
    ) = tryBlock {
        val dto = SubmitTaskDto(
            fileUrls,
            taskId,
            teamId,
            fileNames,
            content,
            title
        )
        taskService.submitTask(dto)
    }
}