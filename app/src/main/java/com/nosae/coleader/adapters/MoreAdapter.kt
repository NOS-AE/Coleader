package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.item_more.*

/**
 * Create by NOSAE on 2020/5/14
 */
class MoreAdapter: ListAdapter<MoreItem, BaseViewHolder>(object : BaseDiffCallback<MoreItem>() {
    override fun areItemsTheSame(oldItem: MoreItem, newItem: MoreItem): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_more)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).run {
            holder.icon.setImageResource(iconRes)
            holder.text.text = text
            holder.itemView.setOnClickListener {
                onClick()
            }
        }
    }
}

data class MoreItem(
    var iconRes: Int,
    var text: String,
    var onClick: () -> Unit
)