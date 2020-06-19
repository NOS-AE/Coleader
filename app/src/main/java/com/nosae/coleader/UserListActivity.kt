package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.FriendsAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.FriendItem
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.databinding.ActivityUserListBinding
import com.nosae.coleader.utils.*
import kotlinx.coroutines.launch

class UserListActivity : BaseActivity<ActivityUserListBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_user_list
    }

    override fun initViews(b: ActivityUserListBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        val teamId = intent.getLongExtra("teamId", -1L)
        if (teamId == -1L) {
            toast("团队ID未知")
            finish()
        }
        b.rv.layoutManager = LinearLayoutManager(this)
        val adapter = FriendsAdapter()
        b.rv.adapter = adapter

        lifecycleScope.launch {
            val list = withIO {
                try {
                    val dto = RetrofitHelper.teamService.getMembers(teamId)
                    if (dto.errno != "0") throw RuntimeException()
                    val res = ArrayList<FriendItem>()
                    dto.data.rows.forEach {
                        res += FriendItem(it.id, it.avatar, "${it.nickname}(${it.username})", it.email)
                    }
                    res
                } catch (e: Exception) {
                    null
                }
            }
            debug("成员列表 ${list?.size ?: 0}")
            adapter.submitList(b.multiStateView, list)
        }
    }

    companion object {
        fun start(ctx: Context?, teamId: Long) {
            ctx?.startActivity<UserListActivity>(Bundle().apply {
                putLong("teamId", teamId)
            })
        }
    }
}
