package com.nosae.coleader.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Create by NOSAE on 2020/6/7
 */
abstract class BaseBottomDialog: BottomSheetDialogFragment() {

    protected lateinit var mView: View
    protected lateinit var behavior: BottomSheetBehavior<*>

    @LayoutRes abstract fun getLayoutId(): Int

    abstract fun View.initView()

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (!this::mView.isInitialized) {
            mView = LayoutInflater.from(context).inflate(getLayoutId(), null)
        }
        dialog.setContentView(mView)
        val parent = mView.parent as View
        parent.setBackgroundColor(0)
        parent.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        behavior = BottomSheetBehavior.from(parent)
        behavior.isHideable = true
        mView.initView()
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroy() {
        super.onDestroy()
        (mView.parent as ViewGroup).removeView(mView)
    }
}