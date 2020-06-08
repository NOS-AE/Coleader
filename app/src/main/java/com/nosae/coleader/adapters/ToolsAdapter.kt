package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.item_tool.*

/**
 * Create by NOSAE on 2020/5/19
 */
class ToolsAdapter: ListAdapter<ToolItem, BaseViewHolder>(object : BaseDiffCallback<ToolItem>() {
    override fun areItemsTheSame(oldItem: ToolItem, newItem: ToolItem): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_tool)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).run {
            holder.icon.setImageResource(iconRes)
            holder.text.text = name
            holder.itemView.setOnClickListener {
                onClick()
            }
        }
    }
}

data class ToolItem(
    var name: String,
    var iconRes: Int,
    var onClick: () -> Unit
)