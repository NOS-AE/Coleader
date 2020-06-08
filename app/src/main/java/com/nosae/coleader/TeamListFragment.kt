package com.nosae.coleader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.TeamListAdapter
import com.nosae.coleader.data.FriendItem
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.viewmodels.FriendsViewModel
import kotlinx.android.synthetic.main.fragment_team_list.*

class TeamListFragment(
    private val viewModel: FriendsViewModel
) : Fragment(R.layout.fragment_team_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv.layoutManager = LinearLayoutManager(context)
        val adapter = TeamListAdapter()
        rv.adapter = adapter
        viewModel.teamList.observe(viewLifecycleOwner) {
            adapter.submitList(multiStateView, it)
        }

        adapter.setOnItemClick {
            TeamActivity.start(context, it)
        }

        multiStateView.setTryOnErrorListener {
            viewModel.getTeams()
        }
    }
}
