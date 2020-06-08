package com.nosae.coleader.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.Date
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.setOnDelayClickListener
import kotlinx.android.synthetic.main.item_date.*

/**
 * Create by NOSAE on 2020/5/22
 */
class DateAdapter(
    private val ctx: Context,
    private val onClick: (Date) -> Unit
) : ListAdapter<Date, BaseViewHolder>(object : BaseDiffCallback<Date>() {
    override fun areItemsTheSame(oldItem: Date, newItem: Date): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_date)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).let {
            holder.tv_intro.text = it.introduction
            holder.tv_time.text = it.endAt
            holder.tv_level.text = when(it.level) {
                0 -> "紧急"
                1 -> "次紧急"
                2 -> "一般紧急"
                3 -> "不紧急"
                4 -> "无所谓"
                else -> "无所谓"
            }
            holder.tv_level.background.setTint(ctx.getColor(when(it.level) {
                0 -> R.color.level0
                1 -> R.color.level1
                2 -> R.color.level2
                3 -> R.color.level3
                else -> R.color.level4
            }))
            holder.container.setOnDelayClickListener { v ->
                onClick(it)
            }
        }
    }
}