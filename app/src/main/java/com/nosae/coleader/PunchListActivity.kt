package com.nosae.coleader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.PunchListAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.PunchEvent
import com.nosae.coleader.databinding.ActivityPunchListBinding
import com.nosae.coleader.utils.postStickyEvent
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.viewmodels.PunchListViewModel
import kotlinx.android.synthetic.main.activity_punch_list.*

class PunchListActivity : BaseActivity<ActivityPunchListBinding>() {

    private val viewModel by viewModels<PunchListViewModel> {
        PunchListViewModel.Factory()
    }
    private lateinit var adapter: PunchListAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_punch_list
    }

    override fun initViews(b: ActivityPunchListBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("打卡列表")
        b.viewModel = viewModel
        viewModel.teamId = intent.getLongExtra("teamId", -1)
        viewModel.isAdmin.value = intent.getBooleanExtra("isAdmin" ,false)
        b.rv.layoutManager = LinearLayoutManager(this)
        adapter = PunchListAdapter {
            postStickyEvent(PunchEvent(it))
            PunchActivity.start(this)
        }
        b.rv.adapter = adapter
        b.fab.setOnClickListener {
            CreatePunchActivity.start(this, viewModel.teamId)
        }
        viewModel.listRes.observe(this) {
            adapter.submitList(multiStateView, it)
        }
        viewModel.getPunches()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PunchActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun start(context: Context?, teamId: Long? = null, isAdmin: Boolean? = null) {
            context.startActivity<PunchListActivity>(Bundle().apply {
                teamId?.let { putLong("teamId", it) }
                isAdmin?.let { putBoolean("isAdmin", it) }
            })
        }
    }
}
