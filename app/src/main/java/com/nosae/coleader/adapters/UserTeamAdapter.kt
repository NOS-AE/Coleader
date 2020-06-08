package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.MultiStateView
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.Team
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.item_user_team.*

/**
 * Create by NOSAE on 2020/5/7
 */
class UserTeamAdapter: ListAdapter<Team, BaseViewHolder>(Callback()) {

    class Callback: DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_user_team)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val bean = currentList[position]
        holder.text1.text = bean.teamName
        holder.text2.text = if (bean.isAdmin) "管理员" else "普通员工"
        holder.text3.text = bean.introduction
    }
}