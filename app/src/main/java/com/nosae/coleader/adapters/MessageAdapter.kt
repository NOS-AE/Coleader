package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.MockMessage
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.item_message_left.*
import kotlinx.android.synthetic.main.item_message_right.*

/**
 * Create by NOSAE on 2020/5/9
 */
class MessageAdapter: ListAdapter<MockMessage, BaseViewHolder>(Callback) {

    object Callback: BaseDiffCallback<MockMessage>() {
        override fun areItemsTheSame(oldItem: MockMessage, newItem: MockMessage): Boolean {
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