package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.observe
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityUserInfoBinding
import com.nosae.coleader.utils.dp
import com.nosae.coleader.utils.setTransparentStatusBar
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.toast
import com.nosae.coleader.viewmodels.UserInfoViewModel

class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    private val viewModel by viewModels<UserInfoViewModel> {
        UserInfoViewModel.Factory()
    }
    private var type = 0

    private val requestDialog by lazy {
        val frame = FrameLayout(this)
        val et = EditText(this)
        frame.addView(et, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            leftMargin = 20.dp
            rightMargin = 20.dp
            topMargin = 40.dp
            bottomMargin = 20.dp
        })
        AlertDialog.Builder(this)
            .setView(frame)
            .setTitle("填写附加信息")
            .setNegativeButton("取消") { _, _ -> }
            .setPositiveButton("确定") { _, _ ->
                val msg = et.text.toString()
                viewModel.sendRequest(msg)
            }.create()
    }

    override fun getLayoutId(): Int {
        setTransparentStatusBar(true)
        return R.layout.activity_user_info
    }

    override fun initViews(b: ActivityUserInfoBinding, savedInstanceState: Bundle?) {
        b.viewModel = viewModel

        type = intent.getIntExtra("type", 0)
        val btn = b.btnSendMessage
        when(type) {
            TYPE_ME -> {
                btn.text = "修改资料"
                btn.setOnClickListener {
                    startActivity<EditInfoActivity>()
                }
            }
            TYPE_USER -> {
                btn.text = "申请添加"
                btn.setOnClickListener {
                    requestDialog.show()
                }
            }
            TYPE_FRIEND -> {
                btn.text = "发送消息"
                btn.setOnClickListener {
                    MessageActivity.start(this, viewModel.id, viewModel.nickname.value!!)
                }
            }
        }


        val adapter = PagerAdapter(supportFragmentManager, listOf(
            UserTeamFragment(viewModel),
            UserAboutFragment(viewModel)
        ))
        b.pager.adapter = adapter
        b.layoutTab.setupWithViewPager(b.pager)
        b.ivBg.setColorFilter(0x4fFFFFFF)

        bindToolbar("")

        viewModel.requestRes.observe(this) {
            toast(it)
        }
        viewModel.id = intent.getLongExtra("id", 0)
        viewModel.getInfo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return true
    }

    class PagerAdapter(
        manager: FragmentManager,
        private val fragments: List<Fragment>
    ): FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "团队"
                1 -> "关于"
                else -> ""
            }
        }

    }

    companion object {
        const val TYPE_ME = 0
        const val TYPE_FRIEND = 1
        const val TYPE_USER = 2
        fun start(ctx: Context, type: Int = TYPE_ME, userId: Long) {
            ctx.startActivity<UserInfoActivity>(Bundle().apply {
                putInt("type", type)
                putLong("id", userId)
            })
        }
    }
}