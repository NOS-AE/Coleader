package com.nosae.coleader.repository

import com.nosae.coleader.data.LoginDto
import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/5/5
 */
class LoginRepo: BaseRepo() {
    private val userService = RetrofitHelper.userService

    var account: String
        set(value) { SharedPref.loginAccount = value }
        get() = SharedPref.loginAccount

    var password: String
        set(value) { SharedPref.userPassword = value }
        get() = SharedPref.userPassword

    suspend fun login(account: String, pw: String) = tryBlock {
        val dto = LoginDto(account, pw)
        userService.login(dto)
    }
}