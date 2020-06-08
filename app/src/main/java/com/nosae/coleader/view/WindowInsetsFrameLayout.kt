package com.nosae.coleader.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout


/**
 * Create by NOSAE on 2020/5/7
 */
class WindowInsetsFrameLayout(ctx: Context, attrs: AttributeSet): FrameLayout(ctx, attrs) {

    init {
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                requestApplyInsets()
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {}
        })
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val childCount = childCount
        for (index in 0 until childCount)
            getChildAt(index).dispatchApplyWindowInsets(insets)

        return insets
    }

}