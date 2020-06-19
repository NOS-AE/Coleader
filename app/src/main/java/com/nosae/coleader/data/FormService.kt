package com.nosae.coleader.data

import com.nosae.coleader.adapters.GMTToFormat
import com.nosae.coleader.adapters.NullToString
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.squareup.moshi.JsonClass
import retrofit2.http.Headers


/**
 * Create by NOSAE on 2020/6/18
 */
interface FormService {
    @Headers("Authorization: ")
    @GET("http://yjcxlr.cn:3000/api/forms/{page}")
    suspend fun getForms(
        @Path("page") page: Int,
        @Query("teamId") teamId: Long,
        @Query("formId") formId: Long? = null
    ): FormListResDto
}

@JsonClass(generateAdapter = true)
data class FormListResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var count: Int, // 1
        var rows: List<Form>
    )
}

@JsonClass(generateAdapter = true)
data class Form(
    var id: Long, // 1
    var title: String, // 测试标题
    var description: String, // 测试描述
    var content: List<Content>,
    @GMTToFormat var createdAt: String, // 2020-06-15T14:08:48.000Z
    var updatedAt: String, // 2020-06-15T14:08:48.000Z
    var teamId: Long, // 1
    var teamName: String, // 猫猫
    var isAdmin: Boolean, // true
    var teamIntroduction: String, // 猫猫创建的团队库
    var params: String, // fbcf2220c17f0be5a877caebb7388201
    @NullToString var teamAvatar: String // null
) {
    @JsonClass(generateAdapter = true)
    data class Content(
        var title: String, // 测试内容3
        var type: String, // text
        var value: List<String>
    )
}