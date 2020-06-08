package com.nosae.coleader

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.CreateTeamEvent
import com.nosae.coleader.databinding.ActivityCreateBinding
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.CreateViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CreateActivity : BaseActivity<ActivityCreateBinding>() {

    private val viewModel by viewModels<CreateViewModel> {
        CreateViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_create
    }

    override fun initViews(b: ActivityCreateBinding, savedInstanceState: Bundle?) {
        bindToolbar("创建团队")
        b.viewModel = viewModel
        viewModel.message.observe(this) {
            it?.let {
                toast(it)
            }
        }
        viewModel.newTeam.observe(this) {
            val event = CreateTeamEvent(it)
            EventBus.getDefault().post(event)
            finish()
            startActivity<TeamActivity>()
        }
    }
}
