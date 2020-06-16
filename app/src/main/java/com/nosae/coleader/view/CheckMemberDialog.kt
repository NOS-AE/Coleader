package com.nosae.coleader.view

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseBottomDialog
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.Friend
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.inflate
import kotlinx.android.synthetic.main.layout_simple_list.view.*

/**
 * Create by NOSAE on 2020/6/7
 */
class CheckMemberDialog(
    private val onCheck: (Boolean, Friend)->Unit
): BaseBottomDialog() {

    private var list: List<Friend> = emptyList()
    private var checkList = emptyArray<Boolean>()
    private lateinit var adapter: RecyclerView.Adapter<BaseViewHolder>

    override fun getLayoutId(): Int {
        return R.layout.layout_list
    }

    override fun View.initView() {
        debug("initView")
        adapter = object : RecyclerView.Adapter<BaseViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val v = parent.inflate(R.layout.item_text_check)
                return BaseViewHolder(v)
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                val v = holder.itemView as CheckedTextView
                list[position].let {
                    v.text = "${it.nickname}(${it.username})"
                    v.isChecked = checkList[position]
                    v.setOnClickListener { _ ->
                        v.toggle()
                        checkList[position] = v.isChecked
                        onCheck(v.isChecked, it)
                    }
                }
            }

            override fun getItemCount(): Int {
                return list.size
            }
        }
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
    }

    fun setData(list: List<Friend>, checkMap: ((Friend)->Boolean)? = null) {
        this.list = list
        checkList = checkMap?.let {
            Array(list.size) { it(list[it]) }
        } ?: Array(list.size) { false }
    }
}