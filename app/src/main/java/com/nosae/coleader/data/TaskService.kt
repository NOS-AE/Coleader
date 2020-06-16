package com.nosae.coleader.data

import com.nosae.coleader.adapters.NullToString
import com.squareup.moshi.JsonClass
import retrofit2.http.*


/**
 * Create by NOSAE on 2020/6/10
 */
interface TaskService {
    @Headers("Authorization: ")
    @POST("api/tasks/new")
    suspend fun createTask(@Body dto: CreateTaskDto): ResponseDto

    @Headers("Authorization: ")
    @GET("api/tasks/{page}")
    suspend fun getTasks(
        @Path("page") page: Int,
        @Query("date") date: String? = null,
        @Query("teamId") teamId: Long? = null,
        @Query("taskId") taskId: Long? = null,
        @Query("isMe") isMe: Boolean? = null,
        @Query("meStatus") meStatus: Int? = null
    ): TaskListResDto

    @Headers("Authorization: ")
    @DELETE("api/tasks")
    suspend fun deleteTask(@Query("taskId") taskId: Long): ResponseDto

    @Headers("Authorization: ")
    @PATCH("api/tasks")
    suspend fun updateTask(@Body dto: UpdateTaskDto): ResponseDto
}

@JsonClass(generateAdapter = true)
data class CreateTaskDto(
    var teamId: Long, // 2
    var startAt: String, // 2020-05-06 10:23:53
    var endAt: String, // 2020-06-30 11:22:53
    var introduction: String, // maomao
    var remark: String, // 没备注
    var content: String, // maomaomao
    var participants: List<Long>
)

@JsonClass(generateAdapter = true)
data class UpdateTaskDto(
    var teamId: Long, // 2
    var startAt: String, // 2020-05-06 10:23:53
    var endAt: String, // 2020-06-30 11:22:53
    var introduction: String, // maomao
    var remark: String, // 有备注
    var content: String, // maomaomao
    var participants: List<Long>,
    var taskId: Long
)

@JsonClass(generateAdapter = true)
data class TaskListResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<Task>
    )
}

@JsonClass(generateAdapter = true)
data class Task(
    var id: Long, // 2
    var publishId: Int, // 2
    var startAt: String, // 2020-05-06T02:23:53.000Z
    var endAt: String, // 2020-06-30T03:22:53.000Z
    var remark: String, // 没备注
    var content: String, // maomaomao
    var introduction: String, // maomao
    var status: Int, // 0
    var createdAt: String, // 2020-06-03T08:18:05.000Z
    var updatedAt: String, // 2020-06-03T08:18:05.000Z
    var teamId: Long, // 2
    var teamName: String, // 石头石头
    var isAdmin: Boolean, // true
    var teamIntroduction: String, // 憨憨创建的团队库
    var files: List<File>,
    @NullToString var teamAvatar: String, // null
    var participants: List<Long>
) {
    @JsonClass(generateAdapter = true)
    data class File(
        var id: Int, // 1
        var url: String, // 1
        var fileName: String // 1
    )
}