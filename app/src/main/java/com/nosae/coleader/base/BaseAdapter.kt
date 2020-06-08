package com.nosae.coleader.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by NOSAE on 2020/4/24
 */
abstract class BaseAdapter<T, B: ViewDataBinding>(ctx: Context):
    RecyclerView.Adapter<BaseAdapter<T, B>.BaseViewHolder>() {

    var mList = mutableListOf<T>()
    private val mInflater = LayoutInflater.from(ctx)

    protected abstract val diffCallback: BaseDiffUtilCallback

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    fun submitList(list: List<T>) {
        diffCallback.newList = list
        diffCallback.oldList = mList
        val diff = DiffUtil.calculateDiff(diffCallback)

        mList.clear()
        mList.addAll(list)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            DataBindingUtil.inflate(
                mInflater, getLayoutId(), parent, false
            ))

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class BaseViewHolder(val mBinding: B): RecyclerView.ViewHolder(mBinding.root)

    abstract inner class BaseDiffUtilCallback(
        var oldList: List<T> = listOf(),
        var newList: List<T> = listOf()
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }
    }
}