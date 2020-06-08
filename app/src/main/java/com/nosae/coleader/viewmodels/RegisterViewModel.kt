package com.nosae.coleader.viewmodels

import android.util.Patterns
import androidx.lifecycle.*
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.RegisterRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * Create by NOSAE on 2020/5/4
 */
class RegisterViewModel(
    private val repo: RegisterRepo
): BaseViewModel() {

    init {

        captchaCountDown(TempData.captchaTime)
    }

    private var _captchaSend = MutableLiveData(0)

    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val captcha = MutableLiveData<String>()
    val captchaSend: LiveData<String> = _captchaSend.map {
        if (it <= 0)
            "发送验证码"
        else
            "${it}s"
    }

    private val _captchaRes = SingleLiveEvent<String>()
    val captchaRes: LiveData<String?> = _captchaRes

    private val _registerRes = SingleLiveEvent<String>()
    val registerRes: LiveData<String?> = _registerRes

    fun sendCaptcha() {
        if (_captchaSend.value!! > 0) {
            return
        }
        val email = email.value
        if (email.isNullOrBlank()) {
            _captchaRes.value = "邮箱未填写"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _captchaRes.value = "邮箱格式错误"
            return
        }


        launchNetwork {
            isLoading.postValue(true)
            val res = repo.sendCaptcha(email)
            isLoading.postValue(false)
            when {
                res == null -> {
                    _captchaRes.postValue("获取验证码失败")
                }
                res.errno == "11003" -> {
                    _captchaRes.postValue("邮箱已存在")
                }
                res.errno == "0" -> {
                    captchaCountDown(60)
                    _captchaRes.call()
                }
                else -> {
                    _captchaRes.postValue("获取验证码失败")
                }
            }
        }
    }

    fun checkInput() {
        when {
            username.value.isNullOrBlank() -> {
                _registerRes.value = "用户名未填写"
            }
            email.value.isNullOrBlank() -> {
                _registerRes.value = "邮箱未填写"
            }
            password.value.isNullOrBlank() -> {
                _registerRes.value = "密码未填写"
            }
            captcha.value.isNullOrEmpty() -> {
                _registerRes.value = "验证码未填写"
            }
            else -> {
                tryRegister()
            }
        }
    }

    private fun tryRegister() {
        launchNetwork {
            isLoading.postValue(true)
            val res = repo.register(username.value!!, password.value!!, email.value!!, captcha.value!!)
            isLoading.postValue(false)
            if (res == null) {
                _registerRes.value = "注册失败"
                return@launchNetwork
            }
            when(res.errno) {
                "0" -> _registerRes.call()
                "11005" -> _registerRes.value = "验证码错误"
                "11006" -> _registerRes.value = "用户已存在"
                else -> _registerRes.value = "注册失败"
            }
        }
    }

    private var captchaTimeJob: Job? = null
    private fun captchaCountDown(start: Int) {
        if (start <= 0)
            return
        captchaTimeJob?.cancel()

        var now = start
        captchaTimeJob = launchNetwork {
            while (true) {
                now--
                TempData.captchaTime = now
                _captchaSend.postValue(now)
                delay(1000)
                if (now <= 0)
                    break
            }
        }
    }

    class Factory: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RegisterViewModel(RegisterRepo()) as T
        }
    }
}