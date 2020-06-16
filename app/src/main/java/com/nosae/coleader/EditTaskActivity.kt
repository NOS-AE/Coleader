package com.nosae.coleader

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.EditTaskEvent
import com.nosae.coleader.data.UpdateTaskEvent
import com.nosae.coleader.databinding.ActivityEditTaskBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.view.CheckMemberDialog
import com.nosae.coleader.viewmodels.EditTaskViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList

@Bus
class EditTaskActivity : BaseActivity<ActivityEditTaskBinding>() {

    private val viewModel by viewModels<EditTaskViewModel> {
        EditTaskViewModel.Factory()
    }
    private lateinit var dialog: CheckMemberDialog
    private var participants : List<Long>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_task
    }

    override fun initViews(b: ActivityEditTaskBinding, savedInstanceState: Bundle?) {
        b.viewModel = viewModel
        val type = intent.getIntExtra("type", CREATE)
        val teamId = intent.getLongExtra("teamId", -1)
        if (teamId == -1L) {
            toast("未知团队ID")
            finish()
        }
        viewModel.teamId = teamId
        viewModel.type.value = type
        viewModel.taskId

        bindToolbar("")
        dialog = CheckMemberDialog { isChecked, bean ->
            if (isChecked) {
                viewModel.memberResMutable.add(bean)
            } else {
                viewModel.memberResMutable.remove(bean)
            }
        }
        b.text4.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            val startAt = viewModel.formatDate(year, month, dayOfMonth, hourOfDay, minute)
                            viewModel.startAt.value = startAt
                        }, 0, 0, true).show()
                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE]).show()
        }
        b.text8.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            val startAt = viewModel.formatDate(year, month, dayOfMonth, hourOfDay, minute)
                            viewModel.endAt.value = startAt
                        }, 0, 0, true).show()
                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE]).show()
        }
        b.text9.setOnClickListener {
            dialog.show(supportFragmentManager, "dialog")
        }
        viewModel.memberRes.observe(this) {
            it?.let {
                viewModel.memberResMutable.value = ArrayList(it)
                participants?.let { ids ->
                    dialog.setData(it) {
                        val contain = ids.contains(it.id)
                        if (!contain) {
                            viewModel.memberResMutable.remove(it)
                        }
                        contain
                    }
                } ?: dialog.setData(it)
            }
        }
        viewModel.res.observe(this) {
            if (it == null) {
                toast("创建成功")
                postEvent(UpdateTaskEvent())
                finish()
            } else {
                toast(it)
            }
        }

        viewModel.getMember()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEdit(event: EditTaskEvent) {
        val task = event.task
        viewModel.run {
            intro.value = task.introduction
            startAt.value = task.startAt
            endAt.value = task.endAt
            remark.value = task.remark
            content.value = task.content
            participants = task.participants
            viewModel.memberRes.selfAssign()
            memberRes.value = memberRes.value
            taskId = task.id
        }
    }

    companion object {
        const val CREATE = 0
        const val UPDATE = 1

        fun start(ctx: Context?, type: Int, teamId: Long) {
            ctx.startActivity<EditTaskActivity>(Bundle().apply {
                putInt("type", type)
                putLong("teamId", teamId)
            })
        }
    }
}
