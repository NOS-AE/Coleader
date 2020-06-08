package com.nosae.coleader.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.EditInfoRepo

/**
 * Create by NOSAE on 2020/5/11
 */
class EditInfoViewModel(
    private val repo: EditInfoRepo
) : BaseViewModel() {

    val userInfo = MutableLiveData(TempData.userInfo!!.data)

    var avatarUri: Uri? = null

    private val _submitRes = SingleLiveEvent<String>()
    val submitRes: LiveData<String?> = _submitRes

    fun submit() {
        launchNetwork {
            val info = userInfo.value!!
            // if (avatarUri != null) {
            //     info.avatar = submitAvatar()
            // }
            val res = repo.submit(info)
            if (res == null || res.errno != "0") {
                _submitRes.postValue("修改失败")
            } else {
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