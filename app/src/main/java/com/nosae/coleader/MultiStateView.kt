package com.nosae.coleader

/**
 * Create by NOSAE on 2020/5/10
 */

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.nosae.coleader.utils.gone
import com.nosae.coleader.utils.visible

/**
 * Created by Xily on 2019/1/21.
 */
class MultiStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val STATE_CONTENT = 0
        const val STATE_EMPTY = 1
        const val STATE_ERROR = 3
        const val STATE_LOADING = 4
    }

    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentViews: MutableList<View> = arrayListOf()

    private var mEmptyViewResId: Int = R.layout.layout_empty
    private var mErrorViewResId: Int = R.layout.layout_error
    private var mLoadingViewResId: Int = R.layout.layout_loading

    private var mViewState = -1
    private val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    private val layoutInflater = LayoutInflater.from(context)
    private var mTryOnErrorListener: (() -> Unit)? = null


    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    fun showContent() {
        if (mViewState == STATE_CONTENT)
            return
        if (mContentViews.isEmpty()) {
            if (childCount > 0)
                for (i in 0 until childCount) {
                    mContentViews.add(getChildAt(i))
                }
            else return
        }
        removeOtherView()
        mContentViews.forEach {
            it.visible()
        }
        mViewState = STATE_CONTENT
    }

    fun showLoading() {
        if (mViewState == STATE_LOADING)
            return
        if (mLoadingView == null) {
            mLoadingView = layoutInflater.inflate(mLoadingViewResId, null)
        }
        removeOtherView()
        addView(mLoadingView, layoutParams)
        mViewState = STATE_LOADING
    }

    fun showError() {
        if (mViewState == STATE_ERROR)
            return
        if (mErrorView == null) {
            mErrorView = layoutInflater.inflate(mErrorViewResId, null).apply {
                setOnClickListener {
                    showLoading()
                    mTryOnErrorListener?.invoke()
                }
            }
        }
        removeOtherView()
        addView(mErrorView, layoutParams)
        mViewState = STATE_ERROR
    }

    fun showEmpty() {
        if (mEmptyView == null) {
            mEmptyView = layoutInflater.inflate(mEmptyViewResId, null)
        }
        removeOtherView()
        addView(mEmptyView, layoutParams)
        mViewState = STATE_EMPTY
    }

    private fun removeOtherView() {
        when (mViewState) {
            STATE_CONTENT -> mContentViews.forEach {
                it.gone()
            }
            STATE_EMPTY -> if (mEmptyView != null) removeView(mEmptyView)
            STATE_ERROR -> if (mErrorView != null) removeView(mErrorView)
            STATE_LOADING -> if (mLoadingView != null) removeView(mLoadingView)
        }
    }

    fun setTryOnErrorListener(action: () -> Unit) {
        mTryOnErrorListener = action
        mNoNetworkView?.setOnClickListener {
            action.invoke()
        }
        mErrorView?.setOnClickListener {
            action.invoke()
        }
    }

    fun getState() = mViewState
}