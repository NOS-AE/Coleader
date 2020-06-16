package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.TaskAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.UpdateTaskEvent
import com.nosae.coleader.databinding.ActivityTaskListBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.viewmodels.TaskListViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Bus
class TaskListActivity : BaseActivity<ActivityTaskListBinding>() {

    private val viewModel by viewModels<TaskListViewModel> {
        TaskListViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_task_list
    }

    override fun initViews(b: ActivityTaskListBinding, savedInstanceState: Bundle?) {
        b.viewModel = viewModel
        val type = intent.getIntExtra("type", USER)
        if (type == TEAM) {
            viewModel.teamId = intent.getLongExtra("teamId", -1)
            if (viewModel.teamId == -1L) {
                toast("未知团队ID")
                finish()
            }
            val isAdmin = intent.getBooleanExtra("isAdmin", false)
            debug("isAdmin $isAdmin")
            viewModel.isAdmin.value = isAdmin
        }
        bindToolbar("")
        setCenterTitle("任务列表")
        b.rv.layoutManager = LinearLayoutManager(this)
        val adapter = TaskAdapter {
            EditTaskActivity.start(this, EditTaskActivity.UPDATE, viewModel.teamId)
        }
        b.rv.adapter = adapter
        b.fab.setOnClickListener {
            EditTaskActivity.start(this, EditTaskActivity.CREATE, viewModel.teamId)
        }
        viewModel.tasksRes.observe(this) {
            adapter.submitList(b.multiStateView, it)
        }
        viewModel.getTask()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateTask(e: UpdateTaskEvent) {
        viewModel.getTask()
    }

    companion object {
        fun start(ctx: Context?) {
            ctx.startActivity<TaskListActivity>(Bundle().apply {
                putInt("type", USER)
            })
        }
        fun start(ctx: Context?, teamId: Long, isAdmin: Boolean) {
            ctx.startActivity<TaskListActivity>(Bundle().apply {
                putInt("type", TEAM)
                putLong("teamId", teamId)
                putBoolean("isAdmin", isAdmin)
            })
        }
        const val USER = 0
        const val TEAM = 1
    }
}
