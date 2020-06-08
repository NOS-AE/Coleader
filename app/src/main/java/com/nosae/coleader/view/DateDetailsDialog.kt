package com.nosae.coleader.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nosae.coleader.EditDateActivity
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.Date
import com.nosae.coleader.data.EditDateEvent
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.invisible
import kotlinx.android.synthetic.main.item_tag.*
import kotlinx.android.synthetic.main.layout_date_details.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Create by NOSAE on 2020/5/27
 */
class DateDetailsDialog: BottomSheetDialogFragment() {

    private lateinit var behavior: BottomSheetBehavior<*>
    private lateinit var mView: View
    private lateinit var date: Date

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val res = super.onCreateDialog(savedInstanceState)
        res.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        return res
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (!this::mView.isInitialized) {
            mView = LayoutInflater.from(context).inflate(R.layout.layout_date_details, null)
        }
        dialog.setContentView(mView)
        val parent = mView.parent as View
        parent.setBackgroundColor(0)
        parent.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        behavior = BottomSheetBehavior.from(parent)
        behavior.isHideable = true

        mView.initView()
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroy() {
        super.onDestroy()
        (mView.parent as ViewGroup).removeView(mView)
    }

    private fun View.initView() {
        tv_name.text = date.teamName
        tv_intro.text = date.introduction
        tv_status.text = when(date.level) {
            0 -> "紧急"
            1 -> "次紧急"
            2 -> "一般紧急"
            3 -> "不紧急"
            4 -> "无所谓"
            else -> "无所谓"
        }
        tv_status.background.setTint(context.getColor(when(date.level) {
            0 -> R.color.level0
            1 -> R.color.level1
            2 -> R.color.level2
            3 -> R.color.level3
            else -> R.color.level4
        }))
        tv_content.text = date.detail
        rv_tags.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        rv_tags.adapter = TagAdapter(date.label)
        if (date.isAdmin) {
            iv_edit.setOnClickListener {
                dismiss()
                EventBus.getDefault().postSticky(EditDateEvent(date))
                EditDateActivity.start(context, date.teamId, EditDateActivity.UPDATE)
            }
        } else {
            iv_edit.invisible()
        }
    }

    fun show(date: Date, manager: FragmentManager, tag: String?) {
        this.date = date
        show(manager, tag)
    }

    private class TagAdapter(
        private val list: List<String>
    ): RecyclerView.Adapter<BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val v = parent.inflate(R.layout.item_tag)
            return BaseViewHolder(v)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            holder.tv_tag.text = list[position]
        }

    }
}