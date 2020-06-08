package com.nosae.coleader.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

/**
 * Create by NOSAE on 2020/5/7
 */

@ContainerOptions(CacheImplementation.SPARSE_ARRAY)
class BaseViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer