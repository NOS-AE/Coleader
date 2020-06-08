package com.nosae.coleader

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import cn.jpush.android.api.JPushInterface
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.data.TempData
import com.nosae.coleader.databinding.ActivityLoginBinding
import com.nosae.coleader.repository.SharedPref
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel> {
        LoginViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews(b: ActivityLoginBinding, savedInstanceState: Bundle?) {
        val vd = this.viewModel
        b.viewModel = viewModel

        b.btnRegister.setOnClickListener {
            startActivity<RegisterActivity>()
        }
        vd.message.observe(this) {
            toast(it)
        }
        vd.response.observe(this) {
            if (it == null) {
                startActivity<MainActivity>()
                finish()
            } else {
                toast(it)
            }
        }
    }

}
