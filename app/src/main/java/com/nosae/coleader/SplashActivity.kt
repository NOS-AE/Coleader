package com.nosae.coleader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nosae.coleader.config.setAlias
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.SharedPref
import com.nosae.coleader.utils.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPref.loginAccount.isBlank() || SharedPref.userPassword.isBlank()) {
            startActivity<LoginActivity>()
        } else {
            getUserInfo()
            startActivity<MainActivity>()
        }
        finish()
    }

    private fun getUserInfo() {
        val ac = SharedPref.loginAccount
        val pw = SharedPref.userPassword
        if (ac.isBlank() || pw.isBlank()) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                TempData.userInfo = RetrofitHelper.userService.getUserInfo()
                setAlias("splash alias")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
