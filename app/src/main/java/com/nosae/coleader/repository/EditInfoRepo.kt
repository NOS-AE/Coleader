package com.nosae.coleader.repository

import com.nosae.coleader.data.Friend
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.UserInfoResDto
import com.nosae.coleader.data.UserUpdateDto
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

/**
 * Create by NOSAE on 2020/5/11
 */
class EditInfoRepo: BaseRepo() {

    private val userService = RetrofitHelper.userService
    private val utilService = RetrofitHelper.utilService

    suspend fun submit(userInfo: Friend) = tryBlock {
        val dto = UserUpdateDto.createFrom(userInfo)
        userService.updateInfo(dto)
    }

    suspend fun submitAvatar(type: String, file: MultipartBody.Part) = tryBlock {
        utilService.uploadFile(type, file)
    }
}