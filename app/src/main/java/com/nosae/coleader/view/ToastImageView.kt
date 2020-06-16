package com.nosae.coleader.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.nosae.coleader.R
import com.nosae.coleader.utils.deviceWidth

/**
 * Create by NOSAE on 2020/6/16
 */
class ToastImageView(ctx: Context, attrs: AttributeSet): AppCompatImageView(ctx, attrs), View.OnLongClickListener {

    private lateinit var toastText: String

    init {
        ctx.obtainStyledAttributes(attrs, R.styleable.ToastImageView).apply {
            getString(R.styleable.ToastImageView_toastText)?.let {
                toastText = it
                setOnLongClickListener(this@ToastImageView)
            }
        }.recycle()
    }

    override fun onLongClick(v: View): Boolean {
        val toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT)
        val location = intArrayOf(0, 0)
        v.getLocationInWindow(location)
        if (location[0] + v.width / 2 < deviceWidth / 2) {
            toast.setGravity(Gravity.TOP or Gravity.START, location[0] + v.width, location[1] + v.height / 2)
        } else {
            toast.setGravity(Gravity.TOP or Gravity.END, deviceWidth - location[0], location[1] + v.height / 2)
        }
        toast.show()
        return true
    }
}