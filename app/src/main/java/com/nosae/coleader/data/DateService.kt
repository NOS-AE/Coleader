package com.nosae.coleader.data

import com.nosae.coleader.adapters.GMTToFormat
import com.nosae.coleader.adapters.NullToString
import com.squareup.moshi.JsonClass
import retrofit2.http.*


/**
 * Create by NOSAE on 2020/5/22
 */

interface DateService {
    @Headers("Authorization: ")
    @POST("api/schedules/new")
    suspend fun createDate(@Body dto: CreateDateDto): ResponseDto

    @Headers("Authorization: ")
    @GET("api/schedules/{page}")
    suspend fun getDate(
        @Path("page") page: Int,
        @Query("date") date: String? = null,
        @Query("teamId") teamId: Long? = null,
        @Query("scheduleId") dateId: Long? = null
    ): DateResDto

    @Headers("Authorization: ")
    @PATCH("api/schedules")
    suspend fun updateDate(@Body dto: UpdateDateDto): ResponseDto

    @Headers("Authorization: ")
    @DELETE("api/schedules")
    suspend fun deleteDate(@Query("teamId") teamId: Long, @Query("scheduleId") scheduleId: Long): ResponseDto
}

@JsonClass(generateAdapter = true)
data class CreateDateDto(
    var teamId: Long, // 1
    var startAt: String, // 2020-05-06 10:23:53
    var endAt: String, // 2020-05-06 11:22:53
    var introduction: String, // maomao
    var detail: String, // maomaomaomao
    var label: List<String>,
    var level: Int // 0
)

@JsonClass(generateAdapter = true)
data class DateResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<Date>
    )
}

@JsonClass(generateAdapter = true)
data class Date(
    var id: Long, // 1
    @GMTToFormat var startAt: String, // 2020-05-11T02:23:53.000Z
    @GMTToFormat var endAt: String, // 2020-05-13T03:22:53.000Z
    var introduction: String, // 日程1
    var detail: String, // 猫猫创建的日程1
    var level: Int, // 0
    var label: List<String>,
    var status: Int, // 1
    var createdAt: String, // 2020-06-02T07:54:25.000Z
    var updatedAt: String, // 2020-06-02T07:54:25.000Z
    var teamId: Long, // 1
    var teamName: String, // 猫猫
    var isAdmin: Boolean, // false
    var teamIntroduction: String, // 猫猫创建的团队库
    @NullToString var teamAvatar: String // null
)

@JsonClass(generateAdapter = true)
data class TimeDateResDto(
    var errno: String, // 0
    var `data`: List<Date>
)

@JsonClass(generateAdapter = true)
data class UpdateDateDto(
    var teamId: Long, // 2
    var startAt: String, // 2020-05-06 10:23:53
    var endAt: String, // 2020-05-06 11:22:53
    var introduction: String, // maomao
    var detail: String, // maomaomaomao
    var label: List<String>,
    var level: Int, // 0
    var scheduleId: Long // 2
)