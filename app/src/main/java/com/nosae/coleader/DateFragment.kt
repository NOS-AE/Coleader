package com.nosae.coleader

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.DateAdapter
import com.nosae.coleader.base.BaseFragment
import com.nosae.coleader.data.UpdateDateEvent
import com.nosae.coleader.databinding.FragmentDateBinding
import com.nosae.coleader.utils.Bus
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.submitList
import com.nosae.coleader.view.DateDetailsDialog
import com.nosae.coleader.viewmodels.DateViewModel
import kotlinx.android.synthetic.main.fragment_date.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

@Bus
class DateFragment: BaseFragment<FragmentDateBinding>() {

    private lateinit var detailsDialog: DateDetailsDialog

    private val viewModel by viewModels<DateViewModel> {
        DateViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_date
    }

    override fun initView(b: FragmentDateBinding, savedInstanceState: Bundle?) {
        b.rv.layoutManager = LinearLayoutManager(context)
        detailsDialog = DateDetailsDialog()
        val adapter = DateAdapter(requireContext()) {
            detailsDialog.show(it, childFragmentManager, "dialog")
        }
        b.rv.adapter = adapter

        val calendar = Calendar.getInstance()
        val curStr = "${calendar.get(Calendar.YEAR)}年${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DATE)}日"
        text3.text = curStr
        tv_all.setOnClickListener {
            CalendarActivity.start(context)
        }

        viewModel.dateRes.observe(this) {
            adapter.submitList(multiStateView, it)
        }
        viewModel.getDate()

        b.multiStateView.setTryOnErrorListener {
            viewModel.getDate()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateDate(e: UpdateDateEvent) {
        viewModel.getDate()
    }
}
