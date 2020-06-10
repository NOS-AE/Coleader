package com.nosae.coleader

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.PunchEvent
import com.nosae.coleader.databinding.ActivityPunchBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.view.PunchDetailsDialog
import com.nosae.coleader.viewmodels.PunchViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        b.tvCheck.setOnClickListener {
            PunchDetailsDialog()
                .show(supportFragmentManager, "dialog")
        }
        viewModel.punchRes.observe(this) {
            it?.let {
                toast(it)
            }
        }
        registerBus(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onGetPunch(e: PunchEvent) {
        val punch = e.punch
        viewModel.punch.value = punch
    }

    override fun onDestroy() {
        unregisterBus(this)
        super.onDestroy()
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
