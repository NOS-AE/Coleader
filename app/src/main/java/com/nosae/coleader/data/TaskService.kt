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

    @Headers("Authorization: ")
    @POST("api/tasks/submit")
    suspend fun submitTask(@Body dto: SubmitTaskDto): ResponseDto
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
    var id: Long, // 1
    var publishId: Long, // 1
    var startAt: String, // 2020-05-06T02:23:53.000Z
    var endAt: String, // 2020-06-30T03:22:53.000Z
    var remark: String, // 没备注
    var content: String, // maomaomao
    var introduction: String, // maomao
    var status: Int, // 0
    var createdAt: String, // 2020-06-17T05:00:58.000Z
    var updatedAt: String, // 2020-06-17T05:00:58.000Z
    var teamId: Long, // 1
    var teamName: String, // 猫猫
    var isAdmin: Boolean, // true
    var teamIntroduction: String, // 猫猫创建的团队库
    var files: List<File>,
    @NullToString var teamAvatar: String // null
) {
    @JsonClass(generateAdapter = true)
    data class File(
        var id: Long, // 2
        var status: Int, // 0
        var username: String, // NOSAE
        var nickname: String, // 强哥
        var userId: Long, // 3
        var url: List<String>?, // null
        var fileName: List<String>?, // null
        @NullToString var content: String, // null
        @NullToString var title: String, // null
        @NullToString var avatar: String // null
    )
}

@JsonClass(generateAdapter = true)
data class SubmitTaskDto(
    var fileUrl: List<String>,
    var taskId: Long, // 2
    var teamId: Long, // 4
    var fileName: List<String>,
    var content: String, // 123
    var title: String // 123
)