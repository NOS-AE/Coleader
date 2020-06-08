package com.nosae.coleader

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.nosae.coleader.base.ToolbarFragmentContainer
import com.nosae.coleader.data.BadgeEvent
import com.nosae.coleader.data.ReceivePunchEvent
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.postStickyEvent
import com.nosae.coleader.utils.setTransparentStatusBar
import com.nosae.coleader.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

class MainActivity : AppCompatActivity(), ToolbarFragmentContainer {

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory()
    }

    private lateinit var badge: Badge

    private val fragmentMap = mapOf(
        R.id.date_fragment to LazyFragment({ DateFragment() }),
        R.id.file_fragment to LazyFragment({ ContactFragment() }),
        R.id.chat_fragment to LazyFragment({ ChatFragment() }),
        R.id.more_fragment to LazyFragment({ MoreFragment() })
    )
    private var curItemId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar(true)
        setContentView(R.layout.activity_main)

        curItemId = savedInstanceState?.getInt("itemId") ?: R.id.date_fragment
        init()

        bottom_nav_main.setOnNavigationItemReselectedListener {}
        bottom_nav_main.setOnNavigationItemSelectedListener {
            select(it.itemId)
            true
        }
        // bottom_nav_main.setupWithNavController(findNavController(R.id.host_fragment))

        EventBus.getDefault().register(this)

        // badge
        val menuView = bottom_nav_main.getChildAt(0) as ViewGroup
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
        badge = QBadgeView(this)
            .bindTarget(itemView)
            .setBadgeGravity(Gravity.TOP or Gravity.END)
            .setBadgeTextSize(12f, true)
            .setBadgePadding(4f, true)
            .setShowShadow(true)
            .setGravityOffset(8f, 4f, true)

        viewModel.punchRes.observe(this) {
            postStickyEvent(ReceivePunchEvent(it))
        }
        viewModel.getLastPunch()
    }

    private fun init() {
        bottom_nav_main.selectedItemId = curItemId
        val curFragmentLazy = fragmentMap[curItemId] ?: return
        if (curFragmentLazy.fragment == null) {
            curFragmentLazy.fragment = curFragmentLazy.lazy.invoke()
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, curFragmentLazy.fragment!!)
            .commit()
    }

    private fun select(itemId: Int) {
        debug("select")
        val toHide = fragmentMap[curItemId]?.fragment ?: return
        val toShowLazy = fragmentMap[itemId] ?: return
        val toShow = toShowLazy.fragment ?: toShowLazy.lazy.invoke()

        val trx = supportFragmentManager.beginTransaction()
            .hide(toHide)
        if (toShowLazy.fragment == null) {
            debug("add fragment")
            toShowLazy.fragment = toShow
            trx.add(R.id.main_container, toShow)
        } else {
            trx.show(toShow)
        }

        trx.commit()
        curItemId = itemId
    }

    override fun bindToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        // super.onSaveInstanceState(outState)
        outState.putInt("itemId", curItemId)
    }

    /**
     * 消息数量
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onBadgeNumberChange(event: BadgeEvent) {
        badge.badgeNumber = event.number
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private class LazyFragment(var lazy: () -> Fragment, var fragment: Fragment? = null)
}
