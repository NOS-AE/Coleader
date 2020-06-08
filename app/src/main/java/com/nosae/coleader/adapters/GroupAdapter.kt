package com.nosae.coleader.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nosae.coleader.utils.dp
import com.nosae.coleader.utils.sp
import com.nosae.coleader.utils.spf
import kotlin.math.min

class GroupDecoration(private val callback: Callback): RecyclerView.ItemDecoration() {

    var mHeight = 30.dp

    private val mPaint = Paint().apply {
        color = 0xFFC5C5C5.toInt()
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val mTextPaint = TextPaint().apply {
        color = 0xff353535.toInt()
        textSize = 14.spf
        isAntiAlias = true
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }


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

    /**
     * 绘制移动的title
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val left = 0f //parent.paddingLeft.toFloat()
        val right = parent.width.toFloat() //parent.width - parent.paddingRight.toFloat()

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (pos == RecyclerView.NO_POSITION || !isFirstInGroup(pos)) continue

            // val params = child.layoutParams as RecyclerView.LayoutParams
            // val cMarginTop = params.topMargin.toFloat()
            val top = child.top - mHeight.toFloat() // - cMarginTop
            val bottom = top + mHeight.toFloat()
            c.drawRect(left, top, right, bottom, mPaint)

            val title = callback.getGroupTitle(pos)
            c.drawText(title, left, top + mHeight / 2 + mTextPaint.baselineShift, mTextPaint)
        }
    }

    /**
     * 绘制固定的title
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val manager = parent.layoutManager as LinearLayoutManager
        val firstPos = manager.findFirstVisibleItemPosition()
        if (firstPos == RecyclerView.NO_POSITION)
            return

        // 标题要跟着组内最后一个滑走
        if (isLastInGroup(firstPos)) {
            val child = parent.findViewHolderForAdapterPosition(firstPos)?.itemView ?: return
            val bottom = min(child.bottom, mHeight).toFloat()
            val top = bottom - mHeight.toFloat()
            val left = 0f // parent.paddingLeft.toFloat()
            val right = parent.width.toFloat() // - parent.paddingRight.toFloat()
            c.drawRect(left, top, right, bottom, mPaint)

            val title = callback.getGroupTitle(firstPos)
            c.drawText(title, left, top + mHeight / 2 + mTextPaint.baselineShift, mTextPaint)
        } else {
            val top = 0f
            val bottom = mHeight.toFloat()
            val left = 0f // parent.paddingLeft.toFloat()
            val right = parent.width.toFloat() // - parent.paddingRight.toFloat()
            c.drawRect(left, top, right, bottom, mPaint)

            val title = callback.getGroupTitle(firstPos)
            c.drawText(title, left, top + mHeight / 2 + mTextPaint.baselineShift, mTextPaint)
        }
    }

    private fun isFirstInGroup(pos: Int): Boolean {
        if (pos < 0)
            return false
        val index = callback.getGroupIndex(pos)
        val preIndex = callback.getGroupIndex(pos - 1)
        return index != preIndex
    }

    private fun isLastInGroup(pos: Int): Boolean {
        if (pos < 0)
            return false
        val index = callback.getGroupIndex(pos)
        val preIndex = callback.getGroupIndex(pos + 1)
        return index != preIndex
    }

    interface Callback {
        fun getGroupIndex(pos: Int): Int
        fun getGroupTitle(pos: Int): String
    }
}

class GroupTitle(
    var posInAdapter: Int,
    var title: String
)