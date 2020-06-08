package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.MultiStateView
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.UserInfo
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.item_user_info.*

/**
 * Create by NOSAE on 2020/5/7
 */
class UserAboutAdapter:  ListAdapter<UserInfo, BaseViewHolder>(Callback()) {

    class Callback : DiffUtil.ItemCallback<UserInfo>() {
        override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_user_info)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val bean = currentList[position]
        holder.text1.text = bean.key
        holder.text2.text = bean.value
    }
}