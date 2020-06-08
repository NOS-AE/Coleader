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
    @Transient var isMember: Boolean = true
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

fun CreateTeamResDto.Data.toTeam() = Team(
    id,
    teamName,
    introduction,
    createdAt,
    updatedAt,
    true,
    ""
)