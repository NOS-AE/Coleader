package com.nosae.coleader.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseBottomDialog
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.submitList
import kotlinx.android.synthetic.main.layout_punch_details.view.*
import kotlinx.android.synthetic.main.layout_simple_list.*
import kotlinx.android.synthetic.main.layout_simple_list.view.*

/**
 * Create by NOSAE on 2020/5/29
 */
class PunchDetailsDialog : BaseBottomDialog() {

    private var list0: List<String>? = emptyList()
    private var list1: List<String>? = emptyList()

    override fun getLayoutId(): Int {
        return R.layout.layout_punch_details
    }

    override fun View.initView() {
        pager.adapter = object : RecyclerView.Adapter<BaseViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val v = parent.inflate(R.layout.layout_list)
                v.rv.layoutManager = LinearLayoutManager(context)
                v.rv.adapter = ItemAdapter()
                return BaseViewHolder(v)
            }

            override fun getItemCount(): Int {
                return 2
            }

            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                (holder.rv.adapter as ItemAdapter).submitList(
                    holder.multiStateView,
                    if (position == 0)
                        list1
                    else
                        list0
                )
            }

        }
        TabLayoutMediator(tab, pager) { tab, pos ->
            tab.text = if (pos == 0)
                "已打卡"
            else
                "未打卡"
        }.attach()
    }

    fun setData(list0: List<String>?, list1: List<String>?) {
        this.list0 = list0
        this.list1 = list1
    }

    private class ItemAdapter :
        ListAdapter<String, BaseViewHolder>(object : BaseDiffCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
        }
    }) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val v = parent.inflate(android.R.layout.simple_list_item_1)
            return BaseViewHolder(v)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            (holder.itemView as TextView).text = getItem(position)
        }

    }

}