package com.nosae.coleader

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.NewFriendAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityNewFriendBinding
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.viewmodels.NewFriendViewModel

class NewFriendActivity : BaseActivity<ActivityNewFriendBinding>() {

    private val viewModel by viewModels<NewFriendViewModel> {
        NewFriendViewModel.Factory()
    }
    private lateinit var mAdapter: NewFriendAdapter

    private lateinit var searchView: SearchView

    override fun getLayoutId(): Int {
        return R.layout.activity_new_friend
    }

    override fun initViews(b: ActivityNewFriendBinding, savedInstanceState: Bundle?) {
        bindToolbar("")

        b.viewModel = viewModel
        b.rv.layoutManager = LinearLayoutManager(this)
        b.rv.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.list_anim)
        b.rv.run {
            layoutManager = LinearLayoutManager(this@NewFriendActivity)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(this@NewFriendActivity, R.anim.list_anim)
            setHasFixedSize(true)
            mAdapter = NewFriendAdapter(viewModel)
            adapter = mAdapter
        }
        b.multiStateView.showEmpty()

        viewModel.searchType.observe(this) {
            if (this::searchView.isInitialized) {
                searchView.queryHint = if (it == 0) "搜索好友" else "搜索团队"
            }
        }
        viewModel.searchRes.observe(this) {
            mAdapter.submitList(b.multiStateView, it)
        }
    }

    override fun getMenuId(): Int? {
        return R.menu.menu_add_friend
    }

    override fun onCreateMenu(menu: Menu) {
        searchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
            .setBackgroundColor(0)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.search(it)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean { return false }

        })
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }
}
