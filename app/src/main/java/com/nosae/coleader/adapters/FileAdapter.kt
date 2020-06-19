package com.nosae.coleader.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nosae.coleader.R
import com.nosae.coleader.base.BaseDiffCallback
import com.nosae.coleader.base.BaseViewHolder
import com.nosae.coleader.utils.gone
import com.nosae.coleader.utils.inflate
import com.nosae.coleader.utils.visible
import kotlinx.android.synthetic.main.item_file.*

/**
 * Create by NOSAE on 2020/6/18
 */

class FileAdapter(
    private val onDelete: (FileItem)->Unit,
    private val onDownload: (FileItem)->Unit
): ListAdapter<FileItem, BaseViewHolder>(object : BaseDiffCallback<FileItem>() {
    override fun areItemsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
        return oldItem.name == newItem.name
    }
}) {

    var status = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = parent.inflate(R.layout.item_file)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).let {
            holder.tv_name.text = it.name
            if (status == 0) {
                holder.tv_download.gone()
                holder.iv_delete.visible()
                holder.iv_delete.setOnClickListener { _ ->
                    onDelete(it)
                }
            } else{
                holder.tv_download.visible()
                holder.iv_delete.gone()
                holder.tv_download.setOnClickListener { _ ->
                    onDownload(it)
                }
            }
        }
    }
}

data class FileItem(
    var name: String,
    var path: String
)