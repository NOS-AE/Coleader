package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.EditInfoRepo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Create by NOSAE on 2020/5/11
 */
class EditInfoViewModel(
    private val repo: EditInfoRepo
) : BaseViewModel() {

    val userInfo = MutableLiveData(TempData.userInfo!!.data.copy())

    var avatarPath: String? = null

    private val _submitRes = SingleLiveEvent<String>()
    val submitRes: LiveData<String?> = _submitRes

    fun submit() {
        launchNetwork {
            val info = userInfo.value!!
            avatarPath?.let {
                val file = File(it)
                val fileBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)
                val res = repo.submitAvatar("image", filePart)
                if (res == null || res.errno != "0") {
                    _submitRes.postValue("修改失败")
                    return@launchNetwork
                } else {
                    res.data.path
                }
            }?.let {
                info.avatar = "http://yjcxlr.cn:3000$it"
            }
            val res = repo.submit(info)
            if (res == null || res.errno != "0") {
                _submitRes.postValue("修改失败")
            } else {
                TempData.userInfo!!.data = info
                _submitRes.postCall()
            }
        }
    }

    // private suspend fun submitAvatar(): String {
    //     val res = repo
    // }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditInfoViewModel(EditInfoRepo()) as T
        }
    }
}