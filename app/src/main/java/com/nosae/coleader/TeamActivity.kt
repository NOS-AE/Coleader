package com.nosae.coleader

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Exception

class TeamActivity : BaseActivity<ActivityTeamBinding>() {

    private val viewModel by viewModels<TeamViewModel> {
        TeamViewModel.Factory()
    }
    private var qrCodeJob: Job? = null
    private val loadingDialog by lazy {
        ProgressDialog(this).apply {
            setMessage("获取二维码中...")
            setOnCancelListener {
                qrCodeJob?.cancel()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_team
    }

    override fun initViews(b: ActivityTeamBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("团队信息")
        b.viewModel = viewModel
        viewModel.teamId = intent.getLongExtra("teamId", -1L)
        if (viewModel.teamId != -1L) {
            viewModel.getInfo()
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
        val list = arrayListOf(
            ToolItem(
                "日程",
                R.drawable.ic_calendar
            ) {
                CalendarActivity.start(this, viewModel.teamId, viewModel.team.value!!.isAdmin)
            }, ToolItem(
                "打卡",
                R.drawable.ic_punch
            ) {
                PunchListActivity.start(this, viewModel.teamId, viewModel.team.value!!.isAdmin)
            }, ToolItem(
                "任务",
                R.drawable.ic_task
            ) {
                TaskListActivity.start(this, viewModel.teamId, viewModel.team.value!!.isAdmin)
            }, ToolItem(
                "发布问卷",
                R.drawable.ic_questionnaire
            ) {

            })
        adapter.submitList(list)
        viewModel.teamUrl.observe(this) {
            if (it == null)
                toast("分享失败")
            else
                openShare(it.url, it.image)
        }
        viewModel.team.observe(this) {
            if (it.isAdmin) {
                list.add(0, ToolItem("分享", R.drawable.ic_share) {
                    viewModel.shareTeam()
                })
                adapter.submitList(list.toList())
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun openShare(url: String, image: String) {
        try {
            loadingDialog.show()
            qrCodeJob = launch(Dispatchers.IO) {
                val base64 = image.split(',')[1]
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                ShareTeamActivity.start(this@TeamActivity, url, bmp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("分享失败")
        } finally {
            loadingDialog.dismiss()
        }
    }

    companion object {
        fun start(ctx: Context?, teamId: Long) {
            ctx.startActivity<TeamActivity>(Bundle().apply {
                putLong("teamId", teamId)
            })
        }
    }
}
