package com.nosae.coleader.repository

import com.nosae.coleader.data.RegisterDto
import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/5/5
 */
class RegisterRepo: BaseRepo() {

    private val userService = RetrofitHelper.userService

    suspend fun register(
        username: String,
        pw: String,
        email: String,
        code: String
    ) = tryBlock {
        val dto = RegisterDto(username, pw, email, code)
        userService.register(dto)
    }

    suspend fun sendCaptcha(email: String) = tryBlock {
        userService.sendCaptcha(email)
    }
}