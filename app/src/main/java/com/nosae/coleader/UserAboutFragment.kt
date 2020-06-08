package com.nosae.coleader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.UserAboutAdapter
import com.nosae.coleader.data.UserInfo
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.viewmodels.UserInfoViewModel
import kotlinx.android.synthetic.main.fragment_user_about.*

class UserAboutFragment(
    private val viewModel: UserInfoViewModel
) : Fragment(R.layout.fragment_user_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv.layoutManager = LinearLayoutManager(context)
        val adapter = UserAboutAdapter()
        rv.adapter = adapter
        viewModel.userInfoList.observe(viewLifecycleOwner) {
            adapter.submitList(multiStateView, it)
        }
        multiStateView.setTryOnErrorListener {
            viewModel.getUserInfo()
        }
    }
}