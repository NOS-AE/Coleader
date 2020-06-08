package com.nosae.coleader.base

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.viewpager.widget.ViewPager
import com.nosae.coleader.R
import com.nosae.coleader.utils.dp
import java.lang.IllegalArgumentException

/**
 * Create by NOSAE on 2019/10/24
 */
class ScaleTab(
    context: Context, attrs: AttributeSet
): LinearLayout(context, attrs) {

    interface OnTabSelectedListener {
        fun onSelected(index: Int, tabText: String)
        fun onUnselected(index: Int, tabText: String)
    }

    private val animDuration = 150L

    private val mPaddingHorizontal = 8.dp
    private val mPaddingVertical = 6.dp
    @ColorInt private var mTextColorSelected: Int = 0
    @ColorInt private var mTextColorNormal: Int = 0
    private var mTextSizeNormal = 0f
    private val mSelectedScale = 1.1f

    private val mTabList = ArrayList<TextView>()
    private var mSelectedIndex = 0

    private var mListener: OnTabSelectedListener? = null
    private var mViewPager: ViewPager? = null

    private val mScaleUpAnimList = ArrayList<AnimatorSet>()
    private val mScaleDownAnimList = ArrayList<AnimatorSet>()

    //util
    private val typedValue = TypedValue()

    init {
        orientation = HORIZONTAL
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleTab)
        mTextColorSelected = ta.getColor(R.styleable.ScaleTab_textColorSelected, 0x000000)
        mTextColorNormal = ta.getColor(R.styleable.ScaleTab_textColorNormal, 0x000000)
        mTextSizeNormal = ta.getDimension(R.styleable.ScaleTab_textSizeNormal, 0f)
        ta.recycle()
    }

    fun addTab(text: String) {
        val view = TextView(context)
        view.text = text
        addView(view)
    }

    fun setCurrentTab(index: Int) {
        if (index == mSelectedIndex) return
        val oldIndex = mSelectedIndex
        mSelectedIndex = index
        setTab(index, oldIndex)
        //viewPager
        mViewPager?.let { pager ->
            pager.currentItem = index
        }
    }

    fun getCurrentTab() = mSelectedIndex

    //exclude viewPager
    private fun setCurrentTabInternal(index: Int) {
        if (index == mSelectedIndex) return
        val oldIndex = mSelectedIndex
        mSelectedIndex = index
        setTab(index, oldIndex)

    }

    //Merely change UI state of the views, include listener
    private fun setTab(index: Int, oldIndex: Int) {
        //listener
        mListener?.onUnselected(oldIndex, mTabList[oldIndex].text.toString())
        mListener?.onSelected(index, mTabList[oldIndex].text.toString())
        //tab ui
        mScaleUpAnimList[index].start()
        mScaleDownAnimList[oldIndex].start()
        mTabList[oldIndex].setTextColor(mTextColorNormal)
        mTabList[index].setTextColor(mTextColorSelected)
    }

    fun setTextColorSelected(color: Int) {
        mTabList[mSelectedIndex].setTextColor(color)
    }

    fun setTextColorNormal(color: Int) {
        mTabList.filter {
            mTabList.indexOf(it) != mSelectedIndex
        }.forEach {
            it.setTextColor(color)
        }
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                setCurrentTabInternal(position)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //TODO 根据positionOffSet联动
            }
        })
    }

    fun setOnTabSelectedListener(listener: OnTabSelectedListener) {
        mListener = listener
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is TextView) {
            modifyView(child)
            super.addView(child, index, params)
        } else {
            throw IllegalArgumentException("The child view of ScaleTab must be TextView!")
        }
    }

    private fun modifyView(textView: TextView) = textView.let {
        mTabList.add(it)

        val scaleUpAnim = AnimatorSet().apply { playTogether(
            ObjectAnimator.ofFloat(it, "scaleX", 1f, mSelectedScale).setDuration(animDuration),
            ObjectAnimator.ofFloat(it, "scaleY", 1f, mSelectedScale).setDuration(animDuration)
        )}
        val scaleDownAnimSet = AnimatorSet().apply {
            playTogether(
            ObjectAnimator.ofFloat(it, "scaleX", mSelectedScale, 1f).setDuration(animDuration),
            ObjectAnimator.ofFloat(it, "scaleY", mSelectedScale, 1f).setDuration(animDuration)
        )}
        mScaleUpAnimList.add(scaleUpAnim)
        mScaleDownAnimList.add(scaleDownAnimSet)

        // context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true)
        // it.setBackgroundResource(typedValue.resourceId)

        it.setPadding(mPaddingHorizontal, mPaddingVertical, mPaddingHorizontal, mPaddingVertical)
        it.setOnClickListener { view ->
            setCurrentTab(mTabList.indexOf(view))
        }
        it.typeface = Typeface.DEFAULT_BOLD
        it.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeNormal)
        if(mTabList.indexOf(it) == mSelectedIndex) {
            it.setTextColor(mTextColorSelected)
            it.scaleX = 1.2f
            it.scaleY = 1.2f
        } else {
            it.setTextColor(mTextColorNormal)
        }
    }
}