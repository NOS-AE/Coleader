package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.nosae.coleader.adapters.ToolItem
import com.nosae.coleader.adapters.ToolsAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityTeamBinding
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.TeamViewModel

class TeamActivity : BaseActivity<ActivityTeamBinding>() {

    private val viewModel by viewModels<TeamViewModel> {
        TeamViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_team
    }

    override fun initViews(b: ActivityTeamBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        b.viewModel = viewModel
        val teamId = intent.getLongExtra("teamId", -1L)
        if (teamId != -1L) {
            viewModel.getInfo(teamId)
        } else {
            toast("团队ID未知")
            finish()
        }
        viewModel.teamRes.observe(this) {
            it?.let {
                toast(it)
            }
        }
        b.rvTools.layoutManager = object : GridLayoutManager(this, 5) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val adapter = ToolsAdapter()
        b.rvTools.adapter = adapter
        val list = listOf(
            ToolItem(
                "日程",
                R.drawable.ic_calendar
            ) {
                CalendarActivity.start(this, teamId, viewModel.team.value!!.isAdmin)
            }, ToolItem(
                "打卡",
                R.drawable.ic_punch
            ) {
                PunchListActivity.start(this, teamId, viewModel.team.value!!.isAdmin)
            }, ToolItem(
                "发布问卷",
                R.drawable.ic_questionnaire
            ) {

            })
        adapter.submitList(list)
    }

    companion object {
        fun start(ctx: Context?, teamId: Long) {
            ctx.startActivity<TeamActivity>(Bundle().apply {
                putLong("teamId", teamId)
            })
        }
    }
}
