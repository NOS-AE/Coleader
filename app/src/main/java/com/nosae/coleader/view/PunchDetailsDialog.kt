package com.nosae.coleader.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.PunchDetails
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.layout_punch_details.view.*
import kotlinx.android.synthetic.main.layout_simple_list.view.*

/**
 * Create by NOSAE on 2020/5/29
 */
class PunchDetailsDialog: BottomSheetDialogFragment() {
    private lateinit var behavior: BottomSheetBehavior<*>
    private lateinit var mView: View
    private lateinit var adapters: List<RecyclerView.Adapter<*>>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val res = super.onCreateDialog(savedInstanceState)
        res.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        return res
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (!this::mView.isInitialized) {
            mView = LayoutInflater.from(context).inflate(R.layout.layout_punch_details, null)
        }
        dialog.setContentView(mView)
        val parent = mView.parent as View
        parent.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        behavior = BottomSheetBehavior.from(parent)
        behavior.isHideable = true

        mView.initView()
    }

    private fun View.initView() {

        pager.adapter = PagerAdapter()

        TabLayoutMediator(tab, pager) { tab, position ->
            tab.text = if (position == 0)
                "已打卡"
            else
                "未打卡"
            pager.currentItem = position
        }.attach()
    }

    private inner class PagerAdapter : RecyclerView.Adapter<BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val v = parent.inflate(R.layout.layout_simple_list)
            v.rv.layoutManager = LinearLayoutManager(context)
            return BaseViewHolder(v)
        }

        override fun getItemCount(): Int {
            return 2
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            (holder.itemView as RecyclerView).adapter = adapters[position]
        }
    }

    private inner class ListAdapter: androidx.recyclerview.widget.ListAdapter<PunchDetails, BaseViewHolder>(object : BaseDiffCallback<PunchDetails>() {
        override fun areItemsTheSame(oldItem: PunchDetails, newItem: PunchDetails): Boolean {
            return oldItem.username == newItem.username
        }
    }) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val v = parent.inflate(android.R.layout.simple_list_item_1)
            return BaseViewHolder(v)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            getItem(position).run {

            }
        }

    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroy() {
        super.onDestroy()
        (mView.parent as ViewGroup).removeView(mView)
    }
}