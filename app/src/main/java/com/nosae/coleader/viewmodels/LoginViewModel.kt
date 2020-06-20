package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.MyApplication
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.config.setAlias
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.LoginRepo
import com.nosae.coleader.repository.SharedPref
import io.socket.client.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Create by NOSAE on 2020/4/22
 */
class LoginViewModel(
    private val repo: LoginRepo
): BaseViewModel() {

    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _response = SingleLiveEvent<String>()
    val response: LiveData<String?> = _response

    init {
        val ac = repo.account
        val pw = repo.password
        if (ac.isNotBlank() && pw.isNotBlank()) {
            account.value = ac
            password.value = pw
        }
    }

    fun checkInput() {
        when {
            account.value.isNullOrBlank() -> _message.value = "账号未填写"
            password.value.isNullOrBlank() -> _message.value = "密码未填写"
            else -> login()
        }
    }

    private fun login() = launchNetwork {
        val ac = account.value!!
        val pw = password.value!!
        isLoading.postValue(true)
        val res = repo.login(ac, pw)
        isLoading.postValue(false)
        if (res == null) {
            _response.postValue("登录失败")
            return@launchNetwork
        }
        if (res.errno != "0") {
            _response.postValue("登录失败")
        } else {
            TempData.token = res.data!!.token
            repo.account = ac
            repo.password = pw
            _response.postValue(null)
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        val ac = SharedPref.loginAccount
        val pw = SharedPref.userPassword
        if (ac.isBlank() || pw.isBlank()) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            TempData.userInfo = RetrofitHelper.userService.getUserInfo()
            setAlias("login alias")
        }
    }

    class Factory: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(LoginRepo()) as T
        }
    }

}