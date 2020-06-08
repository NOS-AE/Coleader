package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.MultiStateView
import com.nosae.coleader.R
import com.nosae.coleader.TeamActivity
import com.nosae.coleader.UserInfoActivity
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.TempData
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.load
import com.nosae.coleader.viewmodels.NewFriendViewModel
import kotlinx.android.synthetic.main.item_new_friend.*

/**
 * Create by NOSAE on 2020/5/10
 */
class NewFriendAdapter(
    private val viewModel: NewFriendViewModel
): ListAdapter<NewFriend, BaseViewHolder>(object : BaseDiffCallback<NewFriend>() {
    override fun areItemsTheSame(oldItem: NewFriend, newItem: NewFriend): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_new_friend)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        holder.iv_avatar.load(item.avatar)
        holder.text1.text = item.text1
        holder.text2.text = item.text2
        holder.itemView.setOnClickListener {
            if (viewModel.searchType.value == 0) {
                val bean = viewModel.userList!![position]
                UserInfoActivity.start(it.context, bean.userType, bean.id)
            } else {
                val bean = viewModel.teamList!![position]
                TeamActivity.start(it.context, bean.id)
            }
        }
    }
}

data class NewFriend(
    var avatar: String,
    var text1: String,
    var text2: String
)