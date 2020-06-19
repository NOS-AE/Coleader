package com.nosae.coleader.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.MessageActivity
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.gone
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.load
import com.nosae.coleader.utils.visible
import kotlinx.android.synthetic.main.item_chat.*

/**
 * Create by NOSAE on 2020/5/8
 */
class ChatAdapter(private val ctx: Context): ListAdapter<ChatItem, BaseViewHolder>(Callback) {

    object Callback: BaseDiffCallback<ChatItem>() {
        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_chat)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).let {
            holder.text1.text = it.name
            holder.text2.text = it.content
            holder.iv_avatar.load(it.avatar)
            if (it.count <= 0) {
                holder.tv_count.gone()
            } else {
                holder.tv_count.visible()
                holder.tv_count.text = if (it.count < 100) it.count.toString() else "99+"
            }
            holder.itemView.setOnClickListener { _ ->
                it.count = 0
                notifyDataSetChanged()
                MessageActivity.start(ctx, it.id, it.name, it.type)
            }
        }
    }
}

data class ChatItem(
    var id: Long,
    var name: String,
    var content: String,
    var avatar: String,
    var count: Int,
    var type: Int
)