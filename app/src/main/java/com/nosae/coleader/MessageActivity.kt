package com.nosae.coleader

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.MessageAdapter
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityMessageBinding

class MessageActivity : BaseActivity<ActivityMessageBinding>() {

    private lateinit var adapter: MessageAdapter

    private val msgList = mutableListOf<MockMessage>()

    private val robotHandler = Handler()

    override fun getLayoutId(): Int {
        return R.layout.activity_message
    }

    override fun initViews(b: ActivityMessageBinding, savedInstanceState: Bundle?) {
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
            sendMessage()
        }

        initToolbar()
    }

    private fun sendMessage() {
        val msg = mBinding.etInput.text.toString()
        if (msg.isNotEmpty()) {
            msgList.add(0, MockMessage(msg, false))
            adapter.submitList(msgList.toList()) {
                mBinding.rv.scrollToPosition(0)
                robotHandler.postDelayed({
                    msgList.add(0, MockMessage("What the fuck are you speaking?", true))
                    adapter.submitList(msgList.toList()) {
                        mBinding.rv.scrollToPosition(0)
                    }
                }, 200)
            }



            mBinding.etInput.text.clear()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun initToolbar() {
        val tb = mBinding.toolbar
        setSupportActionBar(tb)
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        title = "Robert Richard"
    }
}

data class MockMessage(
    var message: String,
    var isOther: Boolean
)