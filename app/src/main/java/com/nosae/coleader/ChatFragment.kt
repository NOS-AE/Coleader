package com.nosae.coleader

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.nosae.coleader.adapters.ChatAdapter
import com.nosae.coleader.base.BaseToolbarFragment
import com.nosae.coleader.data.ReceiveChatEvent
import com.nosae.coleader.data.ReceivePunchEvent
import com.nosae.coleader.data.UpdateLastMessageEvent
import com.nosae.coleader.databinding.FragmentChatBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.viewmodels.ChatViewModel
import kotlinx.android.synthetic.main.item_chat.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 */
@Bus
class ChatFragment : BaseToolbarFragment<FragmentChatBinding>() {

    private val viewModel by viewModels<ChatViewModel> {
        ChatViewModel.Factory()
    }
    private lateinit var adapter: ChatAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat
    }

    override fun initView(b: FragmentChatBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("消息")
        b.rv.layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(requireContext())
        b.rv.adapter = adapter
        b.layoutPunch.run {
            iv_avatar.load(R.drawable.ic_punch_round)
            text1.text = "打卡"
            setOnClickListener {
                PunchListActivity.start(context)
            }
        }
        viewModel.chatRes.observe(this) {
            debug("聊天列表 ${it.size}")
            adapter.submitList(it)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetPunch(e: ReceivePunchEvent) {
        removeStickyEvent(e)
        val punches = e.punches
        mBinding.layoutPunch.text2.text = if (punches == null || punches.isEmpty()) {
            "无打卡事项"
        } else {
            punches[0].introduction
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdate(e: UpdateLastMessageEvent) {
        viewModel.getLastMessage()
    }
}