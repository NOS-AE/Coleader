package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.data.Form
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.visible
import kotlinx.android.synthetic.main.item_form.*

/**
 * Create by NOSAE on 2020/6/18
 */
class FormAdapter(
    private val onWrite: (Form) -> Unit,
    private val onCheck: (Form) -> Unit
) : ListAdapter<Form, BaseViewHolder>(object : BaseDiffCallback<Form>() {

    override fun areItemsTheSame(oldItem: Form, newItem: Form): Boolean {
        return oldItem.id == newItem.id
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_form)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).run {
            holder.tv_intro.text = title
            holder.tv_time.text = createdAt
            holder.iv_write.setOnClickListener {
                onWrite(this)
            }
            if (isAdmin) {
                holder.iv_check.visible()
                holder.iv_check.setOnClickListener {
                    onCheck(this)
                }
            }
        }
    }

}