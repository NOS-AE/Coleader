package com.nosae.coleader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.nosae.coleader.adapters.FriendsAdapter
import com.nosae.coleader.data.FriendItem
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.viewmodels.FriendsViewModel
import kotlinx.android.synthetic.main.fragment_friends_list.*

/**
 * Create by NOSAE on 2020/5/11
 */
class FriendsListFragment(
    private val viewModel: FriendsViewModel
): Fragment(R.layout.fragment_friends_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_request.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        PagerSnapHelper().attachToRecyclerView(rv_request)

        rv.layoutManager = LinearLayoutManager(context)
        val adapter = FriendsAdapter()
        rv.adapter = adapter
        viewModel.friendsList.observe(viewLifecycleOwner) {
            debug("observe friends")
            adapter.submitList(multiStateView, it)
        }
        multiStateView.setTryOnErrorListener {
            viewModel.getFriends()
        }
    }
}