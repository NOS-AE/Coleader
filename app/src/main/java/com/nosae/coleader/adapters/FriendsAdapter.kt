package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.UserInfoActivity
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.FriendItem
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.load
import kotlinx.android.synthetic.main.item_new_friend.*

/**
 * Create by NOSAE on 2020/5/11
 */
class FriendsAdapter: ListAdapter<FriendItem, BaseViewHolder>(object : BaseDiffCallback<FriendItem>() {
    override fun areItemsTheSame(oldItem: FriendItem, newItem: FriendItem): Boolean {
        return oldItem.id == newItem.id
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_new_friend)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).run {
            holder.iv_avatar.load(avatar)
            holder.text1.text = text1
            holder.text2.text = text2
            holder.itemView.setOnClickListener {
                UserInfoActivity.start(it.context, UserInfoActivity.TYPE_FRIEND, id)
            }
        }
    }
}