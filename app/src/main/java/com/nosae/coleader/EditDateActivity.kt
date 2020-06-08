package com.nosae.coleader

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.EditDateEvent
import com.nosae.coleader.data.UpdateDateEvent
import com.nosae.coleader.databinding.ActivityEditDateBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.viewmodels.EditDateViewModel
import kotlinx.android.synthetic.main.item_tag.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class EditDateActivity : BaseActivity<ActivityEditDateBinding>() {

    private val viewModel by viewModels<EditDateViewModel> {
        EditDateViewModel.Factory()
    }
    private val levelDialog by lazy {
        val data = arrayOf("紧急", "次紧急", "一般紧急", "不紧急", "无所谓")
        AlertDialog.Builder(this)
            .setTitle("选择日程紧急程度")
            .setItems(data) { dialog, which ->
                dialog.dismiss()
                viewModel.level.value = which
            }.create()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_date
    }

    override fun initViews(b: ActivityEditDateBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        b.viewModel = viewModel

        val type = intent.getIntExtra("type", CREATE)
        viewModel.type.value = type
        setCenterTitle(if (type == CREATE) "创建日程" else "更新日程")
        viewModel.teamId = intent.getLongExtra("teamId", -1L)
        if (viewModel.teamId == -1L) {
            toast("团队ID未知")
            finish()
        }
        registerBus(this)

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

        b.rvTags.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        val adapter = TagAdapter()
        b.rvTags.adapter = adapter

        b.text6.setOnClickListener {
            levelDialog.show()
        }

        b.text9.setOnClickListener {
            val tag = b.et3.text.toString()
            if (tag.contains(';'))
                toast("不能包含分号")
            else
                viewModel.addTag(tag)
        }

        viewModel.tags.observe(this) {
            adapter.submitList(it)
        }
        viewModel.dateRes.observe(this) {
            if (it == null) {
                postEvent(UpdateDateEvent())
                finish()
            } else {
                toast(it)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onEdit(event: EditDateEvent) {
        event.date.let {
            viewModel.run {
                scheduleId = it.id
                intro.value = it.introduction
                details.value = it.detail
                startAt.value = it.startAt
                endAt.value = it.endAt
                level.value = it.level
                tags.value = ArrayList(it.label)
                type.value = UPDATE
            }
        }
        removeStickyEvent(event)
    }

    override fun onDestroy() {
        unregisterBus(this)
        super.onDestroy()
    }

    private inner class TagAdapter: RecyclerView.Adapter<BaseViewHolder>() {

        private var tags = emptyList<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val v = parent.inflate(R.layout.item_tag)
            return BaseViewHolder(v)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            holder.tv_tag.text = tags[position]
            holder.tv_tag.setOnLongClickListener {
                viewModel.removeTag(position)
                true
            }
        }

        fun submitList(list: List<String>) {
            tags = list
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return tags.size
        }
    }

    companion object {
        fun start(context: Context?, teamId: Long, type: Int) {
            val bundle = Bundle().apply {
                putLong("teamId", teamId)
                putInt("type", type)
            }
            context.startActivity<EditDateActivity>(bundle)
        }
        const val CREATE = 0
        const val UPDATE = 1
    }
}
