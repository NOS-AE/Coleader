package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.utils.debug

/**
 * Create by NOSAE on 2020/5/7
 */
class MoreRepo: BaseRepo() {
    private val userService = RetrofitHelper.userService

    suspend fun getUserInfo() = tryBlock {
        userService.getUserInfo().also { dto ->
            debug(dto.toString())
        }
    }
}