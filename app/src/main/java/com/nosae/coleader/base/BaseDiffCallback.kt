package com.nosae.coleader.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * Create by NOSAE on 2020/5/8
 */
abstract class BaseDiffCallback<T>: DiffUtil.ItemCallback<T>() {
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}