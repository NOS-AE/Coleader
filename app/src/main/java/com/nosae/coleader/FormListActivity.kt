package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.FormAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.FormEvent
import com.nosae.coleader.databinding.ActivityFormListBinding
import com.nosae.coleader.utils.postStickyEvent
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.FormListViewModel

class FormListActivity : BaseActivity<ActivityFormListBinding>() {

    private val viewModel by viewModels<FormListViewModel> {
        FormListViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_form_list
    }

    override fun initViews(b: ActivityFormListBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("问卷列表")
        viewModel.teamId = intent.getLongExtra("teamId", -1)
        viewModel.isAdmin.value = intent.getBooleanExtra("isAdmin", false)
        if (viewModel.teamId == -1L) {
            toast("团队ID未知")
            finish()
        }
        b.rv.layoutManager = LinearLayoutManager(this)
        val adapter = FormAdapter(onWrite = {
            postStickyEvent(FormEvent(it))
            WebActivity.start(this, WebActivity.WRITE)
        }, onCheck = {
            postStickyEvent(FormEvent(it))
            WebActivity.start(this, WebActivity.RESULT)
        })
        b.fab.setOnClickListener {
            WebActivity.start(this, viewModel.teamId)
        }
        b.rv.adapter = adapter
        b.viewModel = viewModel
        viewModel.formListRes.observe(this) {
            adapter.submitList(b.multiStateView, it)
        }
        viewModel.getForms()
    }

    companion object {
        fun start(ctx: Context?, teamId: Long, isAdmin: Boolean = false) {
            ctx.startActivity<FormListActivity>(Bundle().apply {
                putLong("teamId", teamId)
                putBoolean("isAdmin", isAdmin)
            })
        }
    }
}
