package com.nosae.coleader.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nosae.coleader.utils.dp
import com.nosae.coleader.utils.spf

/**
 * Create by NOSAE on 2020/6/18
 */
class HeaderDecoration(
    private val callback: Callback
): RecyclerView.ItemDecoration() {

    private var mHeight = 30.dp

    private val mPaint = Paint().apply {
        color = 0xFFf0f0f0.toInt()
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val mTextPaint = TextPaint().apply {
        color = 0xff353535.toInt()
        textSize = 14.spf
        isAntiAlias = true
        baselineShift = (textSize / 2 - descent()).toInt()
    }

    // 给组内第一个Item增加额外空间来绘制Header
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (isFirstInGroup(pos)) {
            outRect.top = mHeight
        }
    }

    // 绘制Header
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val left = parent.left.toFloat() + parent.paddingLeft
        val right = parent.right.toFloat() - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (pos == RecyclerView.NO_POSITION || !isFirstInGroup(pos)) continue

            val top = child.top - mHeight.toFloat()
            val bottom = top + mHeight.toFloat()
            c.drawRect(left, top, right, bottom, mPaint)

            val title = callback.getGroupTitle(pos)
            c.drawText(title, left + 4.dp, top + mHeight / 2 + mTextPaint.baselineShift, mTextPaint)
        }
    }

    // 判断item是否所在组的内第一个item
    private fun isFirstInGroup(pos: Int): Boolean {
        if (pos < 0)
            return false
        if (pos == 0)
            return true
        val index = callback.getGroupIndex(pos)
        val preIndex = callback.getGroupIndex(pos - 1)
        return index != preIndex
    }

    // 回调获取item所在组的下标和标题
    interface Callback {
        fun getGroupIndex(pos: Int): Int
        fun getGroupTitle(pos: Int): String
    }
}