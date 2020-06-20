package com.nosae.coleader

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.PunchEvent
import com.nosae.coleader.databinding.ActivityPunchBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.utils.biometric.BiometricManager
import com.nosae.coleader.view.PunchDetailsDialog
import com.nosae.coleader.viewmodels.PunchViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Bus
class PunchActivity : BaseActivity<ActivityPunchBinding>() {

    private val viewModel by viewModels<PunchViewModel> {
        PunchViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_punch
    }

    override fun initViews(b: ActivityPunchBinding, savedInstanceState: Bundle?) {
        b.viewModel = viewModel
        setTransparentStatusBar(false)
        bindToolbar("打卡")
        b.toolbar.navigationIcon?.setTint(0xFFFFFFFF.toInt())
        b.container.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.list_anim)

        val dialog = PunchDetailsDialog()
        b.tvCheck.setOnClickListener {
            dialog.show(supportFragmentManager, "dialog")
        }
        viewModel.punchRes.observe(this) {
            it?.let {
                toast(it)
            }
        }
        viewModel.membersRes.observe(this) {
            it?.let {
                debug("memberRes ${it.size}")
            } ?: debug("memberRes null")

            val list0 = it?.filter { it.status == 0 }
            val list1 = it?.filter { it.status == 1 }
            dialog.setData(list0?.map { it.name }, list1?.map { it.name })
        }
        val manager = BiometricManager.from(this)
        b.tvPunch.setOnClickListener {
            if (!manager.isBiometricPromptEnable()) {
                toast("不支持指纹打卡")
                return@setOnClickListener
            }
            manager.authenticate(object : BiometricManager.OnBiometriIndentifyCallback {
                override fun onSuccess(result: BiometricPrompt.AuthenticationResult) {
                    super.onSuccess(result)
                    viewModel.punch()
                }

                override fun onFailed() {
                    super.onFailed()
                    debug("onFailed")
                }

                override fun onError(errCode: Int, reason: String) {
                    super.onError(errCode, reason)
                    debug("onError $errCode $reason")
                }

                override fun onCancel() {
                    super.onCancel()
                    toast("取消验证")
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetPunch(e: PunchEvent) {
        removeStickyEvent(e)
        val punch = e.punch
        viewModel.punch.value = punch
    }

    companion object {
        fun start(context: Context?) {
            if (context is Activity) {
                context.startActivityForResult<PunchActivity>(requestCode = REQUEST_CODE)
                context.overridePendingTransition(R.anim.enter_vertical, R.anim.enter_vertical_avoid_black)
            }
        }

        const val REQUEST_CODE = 10
    }
}
