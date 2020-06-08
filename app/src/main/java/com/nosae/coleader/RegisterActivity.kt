package com.nosae.coleader

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityRegisterBinding
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.RegisterViewModel

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel by viewModels<RegisterViewModel> {
        RegisterViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initViews(b: ActivityRegisterBinding, savedInstanceState: Bundle?) {
        b.viewModel = viewModel
        viewModel.captchaRes.observe(this) { msg ->
            msg?.let {
                toast(it)
            }
        }
        viewModel.registerRes.observe(this) { msg ->
            if (msg == null) {
                toast("注册成功")
                finish()
            } else {
                toast(msg)
            }
        }
    }
}
