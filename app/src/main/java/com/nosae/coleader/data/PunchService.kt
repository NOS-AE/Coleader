package com.nosae.coleader.data
import com.nosae.coleader.adapters.GMTToFormat
import com.nosae.coleader.adapters.NullToString
import com.squareup.moshi.JsonClass
import retrofit2.http.*


/**
 * Create by NOSAE on 2020/5/29
 */

interface PunchService {
    @Headers("Authorization: ")
    @POST("api/cards/new")
    suspend fun createPunch(@Body dto: CreatePunchDto): ResponseDto

    @Headers("Authorization: ")
    @GET("api/cards/status")
    suspend fun getDetails(@Query("teamId") teamId: Long, @Query("cardId") cardId: Long): PunchDetailsResDto

    @Headers("Authorization: ")
    @GET("api/cards/{page}")
    suspend fun getPunches(
        @Path("page") page: Int,
        @Query("teamId") teamId: Long? = null,
        @Query("status") status: Int? = null,
        @Query("isPublished") isPublished: Int? = null, // 传个随便的值表示是自己发布的
        @Query("cardId") cardId: Long? = null,
        @Query("date") date: String? = null
    ): PunchListResDto

    @Headers("Authorization: ")
    @POST("api/cards/punch")
    suspend fun punch(@Body dto: PunchDto): ResponseDto
}

@JsonClass(generateAdapter = true)
data class PunchDetailsResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var count: Int, // 2
        var rows: List<PunchDetails>
    )
}

@JsonClass(generateAdapter = true)
data class PunchDetails(
    var username: String, // hanhan
    var nickname: String, // 憨憨
    var email: String, // 3428098215@qq.com
    var status: Int, // 0
    var avatar: Any // null
)

@JsonClass(generateAdapter = true)
data class CreatePunchDto(
    var teamId: Long, // 1
    var participants: List<Long>,
    var startAt: String, // 2020-05-23 05:52:01
    var endAt: String, // 2020-05-24 05:52:01
    var introduction: String // 打卡一
)

@JsonClass(generateAdapter = true)
data class PunchDto(
    var teamId: Long, // 1
    var cardId: Long // 11
)

@JsonClass(generateAdapter = true)
data class PunchListResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<Punch>
    )
}

@JsonClass(generateAdapter = true)
data class Punch(
    var id: Long, // 2
    @GMTToFormat var startAt: String, // 2020-05-22T21:52:01.000Z
    @GMTToFormat var endAt: String, // 2020-05-23T21:52:01.000Z
    var introduction: String, // 打卡一
    var publishId: Int, // 2
    var createdAt: String, // 2020-06-02T08:32:56.000Z
    var updatedAt: String, // 2020-06-02T08:32:56.000Z
    var teamId: Long, // 2
    var status: Int, // 0
    var teamName: String, // 石头石头
    var teamIntroduction: String, // 憨憨创建的团队库
    @NullToString var teamAvatar: String // null
)