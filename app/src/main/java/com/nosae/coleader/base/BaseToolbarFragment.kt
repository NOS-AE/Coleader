package com.nosae.coleader.base

import androidx.databinding.ViewDataBinding
import kotlinx.android.synthetic.main.activity_edit_date.*
import kotlinx.android.synthetic.main.activity_edit_date.view.*

/**
 * Create by NOSAE on 2020/5/28
 */
abstract class BaseToolbarFragment<T: ViewDataBinding>: BaseFragment<T>() {
    open fun bindToolbar(title: String) {
        val act = activity ?: return
        if (act is ToolbarFragmentContainer) {
            act.bindToolbar(toolbar)
            act.title = title
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            val act = activity ?: return
            if (act is ToolbarFragmentContainer) {
                act.bindToolbar(toolbar)
            }
        }
    }

    fun setTitle(title: String) {
        activity?.title = title
    }

    fun setCenterTitle(title: String) {
        toolbar.toolbar_tv.text = title
    }
}