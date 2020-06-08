package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.Punch
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.item_punch_list.*

/**
 * Create by NOSAE on 2020/6/6
 */
class PunchListAdapter(
    private val onClick: (Punch) -> Unit
): ListAdapter<Punch, BaseViewHolder>(object : BaseDiffCallback<Punch>() {
    override fun areItemsTheSame(oldItem: Punch, newItem: Punch): Boolean {
        return oldItem.id == newItem.id
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_punch_list)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).let {
            holder.tv_intro.text = it.introduction
            holder.tv_time.text = it.endAt
            if (it.status == 0) {
                holder.tv_status.text = "未打卡"
                holder.tv_status.background.setTint(holder.itemView.context.getColor(R.color.colorAccent))
            } else {
                holder.tv_status.text = "已打卡"
                holder.tv_status.background.setTint(holder.itemView.context.getColor(R.color.gray))
            }
            holder.container.setOnClickListener { _ ->
                onClick(it)
            }
        }
    }

}