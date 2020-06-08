package com.nosae.coleader.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.MessageActivity
import com.nosae.coleader.MockChat
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.startActivity
import kotlinx.android.synthetic.main.item_chat.*

/**
 * Create by NOSAE on 2020/5/8
 */
class ChatAdapter(private val ctx: Context): ListAdapter<MockChat, BaseViewHolder>(Callback) {

    object Callback: BaseDiffCallback<MockChat>() {
        override fun areItemsTheSame(oldItem: MockChat, newItem: MockChat): Boolean {
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
            holder.itemView.setOnClickListener {
                ctx.startActivity<MessageActivity>()
            }
        }
    }
}