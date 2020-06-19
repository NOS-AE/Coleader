package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.load
import com.nosae.coleader.utils.visible
import kotlinx.android.synthetic.main.item_message_left.*
import kotlinx.android.synthetic.main.item_message_right.*

/**
 * Create by NOSAE on 2020/5/9
 */
class MessageAdapter: ListAdapter<MessageItem, BaseViewHolder>(Callback) {

    object Callback: BaseDiffCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutId = if (viewType == MESSAGE_LEFT) {
            R.layout.item_message_left
        } else {
            R.layout.item_message_right
        }
        val v = parent.inflate(layoutId)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val viewType = holder.itemViewType
        val item = getItem(position)
        if (viewType == MESSAGE_LEFT) {
            holder.tv_message_l.text = item.message
            holder.iv_avatar_l.load(item.avatar)
            if (item.name.isNotEmpty()) {
                holder.tv_name.visible()
                holder.tv_name.text = item.name
            }
        } else {
            holder.tv_message.text = item.message
        }

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).run {
            if (isOther) {
                MESSAGE_LEFT
            } else {
                MESSAGE_RIGHT
            }
        }
    }

    companion object {
        private const val MESSAGE_LEFT = 0
        private const val MESSAGE_RIGHT = 1
    }
}

data class MessageItem(
    var message: String,
    var avatar: String,
    var name: String,
    var isOther: Boolean
)