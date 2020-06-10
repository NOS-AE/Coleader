package com.nosae.coleader

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.CreatePunchEvent
import com.nosae.coleader.databinding.ActivityCreatePunchBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.view.CheckMemberDialog
import com.nosae.coleader.viewmodels.CreatePunchViewModel

class CreatePunchActivity : BaseActivity<ActivityCreatePunchBinding>() {

    private val viewModel by viewModels<CreatePunchViewModel> {
        CreatePunchViewModel.Factory()
    }
    private lateinit var dialog: CheckMemberDialog

    override fun getLayoutId(): Int {
        return R.layout.activity_create_punch
    }

    override fun initViews(b: ActivityCreatePunchBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("创建打卡事项")
        viewModel.teamId = intent.getLongExtra("teamId", -1L)
        if (viewModel.teamId == -1L) {
            toast("团队ID未知")
            finish()
        } else {
            viewModel.getMember()
        }
        b.viewModel = viewModel
        dialog = CheckMemberDialog(viewModel.memberRes) { isCheck, bean ->
            if (isCheck) {
                viewModel.memberResMutable.add(bean)
            } else {
                viewModel.memberResMutable.remove(bean)
            }
        }
        viewModel.memberRes.observe(this) {
            viewModel.memberResMutable.value = it?.let { ArrayList(it) }
        }
        b.text9.setOnClickListener {
            dialog.show(supportFragmentManager, "dialog")
        }
        b.text4.setOnClickListener {
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            val startAt = viewModel.formatDate(year, month, dayOfMonth, hourOfDay, minute)
                            viewModel.startAt.value = startAt
                        }, 0, 0, true).show()
                }, 2020, 6, 1).show()
        }
        b.text8.setOnClickListener {
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            val endAt = viewModel.formatDate(year, month, dayOfMonth, hourOfDay, minute)
                            viewModel.endAt.value = endAt
                        }, 0, 0, true).show()
                }, 2020, 6, 1).show()
        }
        viewModel.createRes.observe(this) {
            if (it == null) {
                toast("创建成功")
                postEvent(CreatePunchEvent())
                finish()
            } else {
                toast(it)
            }
        }
    }

    companion object {
        fun start(ctx: Context?, teamId: Long) {
            ctx.startActivity<CreatePunchActivity>(Bundle().apply {
                putLong("teamId", teamId)
            })
        }
    }
}