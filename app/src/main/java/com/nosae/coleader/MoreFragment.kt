package com.nosae.coleader

import android.app.ActivityOptions
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.MoreAdapter
import com.nosae.coleader.adapters.MoreItem
import com.nosae.coleader.base.BaseFragment
import com.nosae.coleader.data.TempData
import com.nosae.coleader.databinding.FragmentMoreBinding
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.deviceWidth
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.viewmodels.MoreViewModel
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : BaseFragment<FragmentMoreBinding>() {

    private val viewModel by viewModels<MoreViewModel> {
        MoreViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_more
    }

    override fun initView(b: FragmentMoreBinding, savedInstanceState: Bundle?) {
        b.viewModel = this.viewModel
        b.ivAvatar.setColorFilter(0x33ffffff)
        b.fab.setOnClickListener {
            startActivity<EditInfoActivity>()
        }
        // b.appbar.layoutParams.width = deviceWidth
        // b.ivAvatar.setOnClickListener {
        //     TempData.userInfo?.data?.let {
        //         UserInfoActivity.start(requireContext(), UserInfoActivity.TYPE_ME, it.id)
        //         activity?.overridePendingTransition(R.anim.enter_vertical, R.anim.enter_vertical_avoid_black)
        //     } ?: debug("id null")
        //
        // }
        // b.rv.layoutManager = LinearLayoutManager(context)
        // val items = listOf(
        //     MoreItem(R.drawable.ic_punch, "打卡") {
        //
        //     },
        //     MoreItem(R.drawable.ic_questionnaire, "问卷调查") {
        //
        //     }
        // )
        // b.rv.adapter = MoreAdapter().apply {
        //     submitList(items)
        // }
    }
}
