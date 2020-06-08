package com.nosae.coleader.data

import com.squareup.moshi.JsonClass

/**
 * Create by NOSAE on 2020/5/5
 */
@JsonClass(generateAdapter = true)
data class ResponseDto(
    var errno: String
)