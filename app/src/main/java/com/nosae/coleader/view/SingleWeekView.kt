package com.nosae.coleader.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.WeekView
import com.nosae.coleader.utils.dpf
import kotlin.math.max

/**
 * Create by NOSAE on 2020/5/27
 */
class SingleWeekView(ctx: Context): WeekView(ctx) {

    private var selectedBgWidth = 0f
    private var selectedBgRadius = 6.dpf

    override fun onPreviewHook() {
        val rect = Rect()
        mSelectTextPaint.getTextBounds("88", 0, 2, rect)
        selectedBgWidth = max(rect.width(), rect.height()).toFloat() + 12.dpf
    }

    override fun onDrawText(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val cx = x + mItemWidth / 2f
        val baselineY = mTextBaseLine
        val day = calendar.day.toString()

        when {
            isSelected -> {
                canvas.drawText(day, cx, baselineY, mSelectTextPaint)
            }
            hasScheme -> {

            }
            else -> {
                canvas.drawText(day, cx, baselineY, when {
                    calendar.isCurrentDay -> mCurDayTextPaint
                    calendar.isCurrentMonth -> mCurMonthTextPaint
                    else -> mOtherMonthTextPaint
                })
            }
        }
    }

    override fun onDrawSelected(
        canvas: Canvas,
        calendar: Calendar?,
        x: Int,
        hasScheme: Boolean
    ): Boolean {
        val left = x + (mItemWidth - selectedBgWidth) / 2f
        val right = left + selectedBgWidth
        val top = (mItemHeight - selectedBgWidth) / 2f
        val bottom = top + selectedBgWidth
        canvas.drawRoundRect(left, top, right, bottom, selectedBgRadius, selectedBgRadius, mSelectedPaint)
        return true
    }

    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int) {
    }
}