package com.nosae.coleader.data

import com.nosae.coleader.adapters.NullToString
import com.squareup.moshi.JsonClass
import retrofit2.http.*


/**
 * Create by NOSAE on 2020/5/7
 */
interface TeamService {
    @Headers("Authorization: ")
    @GET("api/teams/{page}")
    suspend fun getTeams(@Path("page") page: Int, @Query("userId") userId: Long? = null): TeamResDto

    @Headers("Authorization: ")
    @GET("api/teams/search/{page}")
    suspend fun searchTeams( @Path("page") page: Int, @Query("kw") keyword: String): SearchTeamResDto

    @Headers("Authorization: ")
    @POST("api/teams/new")
    suspend fun createTeam(@Body dto: CreateTeamDto): CreateTeamResDto

    @Headers("Authorization: ")
    @GET("api/teams/detail")
    suspend fun getTeamInfo(@Query("teamId") teamId: Long): TeamInfoResDto

    @Headers("Authorization: ")
    @GET("api/teams/users")
    suspend fun getMembers(@Query("teamId") teamId: Long): TeamMemberResDto

    @Headers("Authorization: ")
    @GET("api/teams/utils/join")
    suspend fun getTeamUrl(@Query("teamId") teamId: Long): JoinTeamUrlResDto
}

@JsonClass(generateAdapter = true)
data class TeamResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<Team>
    )
}
@JsonClass(generateAdapter = true)
data class Team(
    var id: Long, // 2
    var teamName: String, // maomao
    var introduction: String, // maomao
    var createdAt: String, // 2020-05-08T04:51:45.000Z
    var updatedAt: String, // 2020-05-08T04:51:45.000Z
    var isAdmin: Boolean = true, // true
    @NullToString var avatar: String
)

@JsonClass(generateAdapter = true)
data class SearchTeamResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<SearchTeam>
    )
}
@JsonClass(generateAdapter = true)
data class SearchTeam(
    var id: Long, // 8
    var teamName: String, // 强2
    var introduction: String, // 强哥真的强
    var createdAt: String, // 2020-05-11T00:30:59.000Z
    var updatedAt: String, // 2020-05-11T00:30:59.000Z
    @NullToString var avatar: String // null
)

@JsonClass(generateAdapter = true)
data class CreateTeamDto(
    var teamName: String, // 123
    var introduction: String // 123
)


@JsonClass(generateAdapter = true)
data class CreateTeamResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var id: Long, // 5
        var teamName: String, // 123
        var introduction: String, // 123
        var updatedAt: String, // 2020-05-14T06:04:40.799Z
        var createdAt: String // 2020-05-14T06:04:40.799Z
    )
}

@JsonClass(generateAdapter = true)
data class TeamInfoResDto(
    var errno: String, // 0
    var `data`: TeamInfo
)

@JsonClass(generateAdapter = true)
data class TeamInfo(
    var id: Int, // 2
    var teamName: String, // 石头石头
    var introduction: String, // 憨憨创建的团队库
    @NullToString var avatar: String, // null
    var createdAt: String, // 2020-05-13T14:26:45.000Z
    var updatedAt: String, // 2020-05-13T14:26:45.000Z
    var peopleCount: Int, // 3
    var isAdmin: Boolean,
    var isMember: Boolean
)

@JsonClass(generateAdapter = true)
data class TeamMemberResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var count: Int, // 2
        var rows: List<Friend>
    )
}

@JsonClass(generateAdapter = true)
data class JoinTeamUrlResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var url: String, // http://yjcxlr.cn:3000/join/b78631358da4de2c2d9bbacf3e156d99
        var image: String // data:image/png;base64,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
    )
}

fun CreateTeamResDto.Data.toTeam() = Team(
    id,
    teamName,
    introduction,
    createdAt,
    updatedAt,
    true,
    ""
)