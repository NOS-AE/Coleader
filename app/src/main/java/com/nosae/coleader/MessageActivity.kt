package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.MessageAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.ReceiveTeamMessageEvent
import com.nosae.coleader.data.ReceiveUserMessageEvent
import com.nosae.coleader.data.UpdateLastMessageEvent
import com.nosae.coleader.databinding.ActivityMessageBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.viewmodels.MessageViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Bus
class MessageActivity : BaseActivity<ActivityMessageBinding>() {

    private val viewModel by viewModels<MessageViewModel> { MessageViewModel.Factory() }

    private lateinit var adapter: MessageAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_message
    }

    override fun initViews(b: ActivityMessageBinding, savedInstanceState: Bundle?) {
        viewModel.name = intent.getStringExtra("name")!!
        viewModel.type = intent.getIntExtra("type", USER)
        viewModel.id = intent.getLongExtra("id", -1)
        if (viewModel.id == -1L) {
            toast("未知ID")
            finish()
        }
        viewModel.getMessage()
        bindToolbar(viewModel.name)
        b.rv.layoutManager = LinearLayoutManager(this@MessageActivity, LinearLayoutManager.VERTICAL, true).apply {
            stackFromEnd = true
        }
        b.rv.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                b.rv.post { b.rv.scrollToPosition(0) }
            }
        }
        adapter = MessageAdapter()
        b.rv.adapter = adapter

        b.ivSend.setOnClickListener {
            val msg = b.etInput.text.toString()
            mBinding.etInput.text.clear()
            viewModel.send(msg)
        }

        viewModel.msgRes.observe(this) {
            debug("send msg ${it.size}")
            adapter.submitList(it.toList()) {
                mBinding.rv.scrollToPosition(0)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserMessage(e: ReceiveUserMessageEvent) {
        val obj = e.msg
        viewModel.receiveUserMsg(obj)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTeamMessage(e: ReceiveTeamMessageEvent) {
        val obj = e.msg
        viewModel.receiveTeamMsg(obj)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun finish() {
        postEvent(UpdateLastMessageEvent())
        super.finish()
    }

    companion object {
        fun start(ctx: Context?, id: Long, name: String, type: Int = 0) {
            ctx.startActivity<MessageActivity>(Bundle().apply {
                putLong("id", id)
                putString("name", name)
                putInt("type", type)
            })
        }
        const val USER = 0
        const val TEAM = 1
    }
}