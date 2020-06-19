package com.nosae.coleader.repository

import com.nosae.coleader.data.RetrofitHelper

/**
 * Create by NOSAE on 2020/6/18
 */
class FormRepo: BaseRepo() {
    private val service = RetrofitHelper.formService

    suspend fun getForms(teamId: Long, page: Int = 1) = tryBlock {
        service.getForms(page, teamId)
    }
}