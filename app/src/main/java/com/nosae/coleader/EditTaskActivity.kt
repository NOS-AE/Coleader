package com.nosae.coleader

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.UpdateTaskEvent
import com.nosae.coleader.databinding.ActivityEditTaskBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.view.CheckMemberDialog
import com.nosae.coleader.viewmodels.EditTaskViewModel
import java.util.*
import kotlin.collections.ArrayList

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
        val teamId = intent.getLongExtra("teamId", -1)
        if (teamId == -1L) {
            toast("未知团队ID")
            finish()
        }
        viewModel.teamId = teamId
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
                if (participants != null) {
                    dialog.setData(it) {
                        val contain = participants!!.contains(it.id)
                        if (!contain) {
                            viewModel.memberResMutable.remove(it)
                        }
                        contain
                    }
                } else {
                    viewModel.memberResMutable.clear()
                    dialog.setData(it)
                }
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

    companion object {
        const val CREATE = 0
        fun start(ctx: Context?, type: Int, teamId: Long) {
            ctx.startActivity<EditTaskActivity>(Bundle().apply {
                putInt("type", type)
                putLong("teamId", teamId)
            })
        }
    }
}
