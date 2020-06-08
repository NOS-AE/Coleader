package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.nosae.coleader.adapters.DateAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.UpdateDateEvent
import com.nosae.coleader.databinding.ActivityCalendarBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.view.DateDetailsDialog
import com.nosae.coleader.viewmodels.CalendarViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CalendarActivity : BaseActivity<ActivityCalendarBinding>() {

    private lateinit var detailsDialog: DateDetailsDialog

    private val viewModel by viewModels<CalendarViewModel> {
        CalendarViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_calendar
    }

    override fun initViews(b: ActivityCalendarBinding, savedInstanceState: Bundle?) {
        registerBus(this)
        bindToolbar("")
        b.viewModel = viewModel

        val type = intent.getIntExtra("type", USER)
        val isAdmin = intent.getBooleanExtra("isAdmin", false)
        viewModel.type.value = type
        viewModel.isAdmin.value = isAdmin
        when (type) {
            USER -> {
                b.fab.gone()
            }
            TEAM -> {
                val teamId = intent.getLongExtra("teamId", -1)
                if (teamId == -1L) {
                    toast("团队ID未知")
                    finish()
                }
                b.fab.setOnClickListener {
                    EditDateActivity.start(this, teamId, EditDateActivity.CREATE)
                }
                viewModel.teamId = teamId
            }
        }

        detailsDialog = DateDetailsDialog()
        b.rv.layoutManager = LinearLayoutManager(this)
        b.rv.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.list_anim)
        val adapter = DateAdapter(this) {
            detailsDialog.show(it, supportFragmentManager, "dialog")
        }
        b.rv.adapter = adapter

        b.calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar, isClick: Boolean) {
                val curStr = "${calendar.year}年${calendar.month}月"
                b.tvYearMonth.text = curStr
                viewModel.getDate(calendar)
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {}

        })
        b.calendarView.scrollToCurrent()

        viewModel.getDate(b.calendarView.selectedCalendar)
        viewModel.dateRes.observe(this) {
            debug(it.toString())
            adapter.submitList(b.multiStateView, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBus(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onUpdateCalendar(e: UpdateDateEvent) {
        viewModel.retry()
    }

    companion object {
        const val USER = 0
        const val TEAM = 1
        fun start(context: Context?) {
            context.startActivity<CalendarActivity>(Bundle().apply {
                putInt("type", USER)
            })
        }
        fun start(context: Context?, teamId: Long, isAdmin: Boolean) {
            context.startActivity<CalendarActivity>(Bundle().apply {
                putInt("type", TEAM)
                putLong("teamId", teamId)
                putBoolean("isAdmin", isAdmin)
            })
        }
    }
}
