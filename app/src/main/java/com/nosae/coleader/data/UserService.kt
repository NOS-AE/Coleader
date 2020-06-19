package com.nosae.coleader.data

import com.nosae.coleader.adapters.NullToInt
import com.nosae.coleader.adapters.NullToString
import com.squareup.moshi.JsonClass
import retrofit2.http.*

/**
 * Create by NOSAE on 2020/5/5
 */

interface UserService {
    @POST("api/users/register")
    suspend fun register(@Body dto: RegisterDto): ResponseDto

    @POST("api/users/login")
    suspend fun login(@Body dto: LoginDto): LoginResDto

    @GET("api/users/utils/emailCode")
    suspend fun sendCaptcha(@Query("email") email: String): ResponseDto

    @Headers("Authorization: ")
    @GET("api/users")
    suspend fun getUserInfo(@Query("userId") id: String? = null): UserInfoResDto?

    @Headers("Authorization: ")
    @GET("api/users/all/{page}")
    suspend fun searchUsers(@Path("page") page: Int, @Query("kw") keyword: String): SearchUserResDto?

    @Headers("Authorization: ")
    @GET("api/users/friends")
    suspend fun getFriends(): FriendsResDto?

    @Headers("Authorization: ")
    @POST("api/users/friends/request")
    suspend fun sendFriendRequest(@Body dto: FriendRequestDto): ResponseDto

    @Headers("Authorization: ")
    @PATCH("api/users")
    suspend fun updateInfo(@Body info: UserUpdateDto): UserInfoResDto?

    @Headers("Authorization: ")
    @GET("api/users/utils/beat")
    suspend fun beat(): ResponseDto

    @Headers("Authorization: ")
    @GET("api/users/utils/chats")
    suspend fun getLastMessage(): LastUserMessageResDto

    @Headers("Authorization: ")
    @GET("api/users/utils/chat/{page}")
    suspend fun getMessage(@Path("page") page: Int, @Query("toId") toId: Long): UserMessageResDto
}

@JsonClass(generateAdapter = true)
data class RegisterDto(
    var username: String,
    var password: String,
    var email: String,
    var emailCode: String
)

@JsonClass(generateAdapter = true)
data class LoginDto(
    var username: String,
    var password: String
)

@JsonClass(generateAdapter = true)
data class LoginResDto(
    var errno: String,
    var `data`: Data? = null
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var token: String // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJpbnV5YXNoYSIs    InBhc3N3b3JkIjoiMTIzIiwibmlja25hbWUiOiJpbnV5YXNoYSIsImF2YXRhciI6bnVsbCwiZW1ha    WwiOiIxNDg2ODM1MDk3QHFxLmNvbSIsInN0YXR1cyI6MSwiY3JlYXRlZEF0IjoiMjAyMC0wNS0wNFQxNTow    Do0NS4wMDBaIiwidXBkYXRlZEF0IjoiMjAyMC0wNS0wNFQxNTowNDo0NS4wMDBaIiwiaWF0IjoxNTg4NjA    ODU4LCJleHAiOjE1ODg2MDg0NTh9.AwiDNQ272wQYxygsiEktnC5AM5MWMRxgB2htKUJMlHg
    )
}

@JsonClass(generateAdapter = true)
data class UserInfoResDto(
    var errno: String, // 0
    var `data`: Friend
)

@JsonClass(generateAdapter = true)
data class FriendsResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<Friend>
    )
}
@JsonClass(generateAdapter = true)
data class Friend(
    var id: Long, // 2
    var username: String, // NOSAE
    var nickname: String, // 强哥
    var email: String, // 2475945868@qq.com
    var createdAt: String, // 2020-05-09T03:33:47.000Z
    var updatedAt: String, // 2020-05-09T03:33:47.000Z
    @NullToString var avatar: String, // null
    @NullToString var province: String,
    @NullToString var city: String,
    @NullToInt var gender: Int,
    @NullToString var address: String,
    @NullToString var signature: String
)

@JsonClass(generateAdapter = true)
data class SearchUserResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var rows: List<SearchUser>
    )
}
@JsonClass(generateAdapter = true)
data class SearchUser(
    var id: Long, // 2
    var username: String, // NOSAE
    var nickname: String, // 强哥
    var email: String, // 2475945868@qq.com
    var createdAt: String, // 2020-05-09T03:33:47.000Z
    var updatedAt: String, // 2020-05-09T03:33:47.000Z
    @NullToString var avatar: String, // null
    @NullToString var province: String,
    @NullToString var city: String,
    @NullToInt var gender: Int,
    @NullToString var address: String,
    @NullToString var signature: String,
    var userType: Int
)

@JsonClass(generateAdapter = true)
data class FriendRequestDto(
    var receiveId: Long, // 3
    var requestMsg: String // 傻瓜石头,敢不接受我的好友请求?
)

@JsonClass(generateAdapter = true)
data class UserUpdateDto(
    var nickname: String? = null,
    var avatar: String? = null,
    var gender: Int? = null,
    var city: String? = null,
    var province: String? = null,
    var address: String? = null,
    var signature: String? = null
) {
    companion object {
        fun createFrom(friend: Friend) = friend.run {
            UserUpdateDto(
                nickname,
                avatar,
                gender,
                city,
                province,
                address,
                signature
            )
        }
    }
}

data class UserResultMessage(
    var id: Long, // 71
    var toId: Long, // 1
    var chatRecord: String, // 1232lads
    var isRead: Int, // 0
    var type: String, // text
    var updatedAt: String, // 2020-06-17T05:50:06.107Z
    var createdAt: String, // 2020-06-17T05:50:06.107Z
    var userInfo: Info
) {
    data class Info(
        var id: Long,
        var username: String,
        var nickname: String,
        @NullToString var avatar: String
    )
}

@JsonClass(generateAdapter = true)
data class LastUserMessageResDto(
    var errno: String, // 0
    var `data`: List<Data>
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var userId: Long, // 1
        var toId: Long, // 3
        var count: Int, // 0
        var records: Records
    ) {
        @JsonClass(generateAdapter = true)
        data class Records(
            var count: Int, // 18
            var rows: List<LastUserMessage>
        )
    }
}

@JsonClass(generateAdapter = true)
data class LastUserMessage(
    var chatRecord: String, // 1232lads
    var type: String, // text
    var createdAt: String, // 2020-06-17T05:50:06.000Z
    var userId: Long, // 2
    var toId: Long, // 1
    var id: Long // 71
)

@JsonClass(generateAdapter = true)
data class UserMessageResDto(
    var errno: String, // 0
    var `data`: List<Data>
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var userId: Int, // 1
        var toId: Int, // 2
        var count: Int, // 29
        var records: Records
    ) {
        @JsonClass(generateAdapter = true)
        data class Records(
            var count: Int, // 68
            var rows: List<UserMessage>
        )
    }
}

@JsonClass(generateAdapter = true)
data class UserMessage(
    var chatRecord: String, // 1232lads
    var type: String, // text
    var createdAt: String, // 2020-06-17T05:50:06.000Z
    var userId: Long, // 2
    var toId: Long, // 1
    var id: Long // 71
)