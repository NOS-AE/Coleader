package com.nosae.coleader.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nosae.coleader.utils.dp
import com.nosae.coleader.utils.sp
import com.nosae.coleader.utils.spf

/**
 * Create by NOSAE on 2020/5/8
 */
class LoadMoreDecoration: RecyclerView.ItemDecoration() {

    private val textPaint = TextPaint().apply {
        color = 0xFFAAAAAA.toInt()
        isAntiAlias = true
        textSize = 14.spf
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }
    private val bottomHeight = 30.dp

    private var l: (() -> Unit)? = null
    private var bottomVisible = false

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter ?: return
        val pos = parent.getChildAdapterPosition(view)
        if (pos == adapter.itemCount - 1) {
            outRect.bottom = bottomHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val adapter = parent.adapter ?: return
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (pos == adapter.itemCount - 1) {
                val x = parent.width / 2f
                val y = child.bottom + bottomHeight / 2f
                c.drawText("你滑到底了~", x, y + textPaint.baselineShift, textPaint)

                if (!bottomVisible) {
                    bottomVisible = true
                    l?.invoke()
                }
            } else if (i == childCount - 1) {
                bottomVisible = false
            }
        }
    }

    fun setListener(l: () -> Unit) {
        this.l = l
    }
}