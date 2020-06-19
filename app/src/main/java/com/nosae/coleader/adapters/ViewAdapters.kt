package com.nosae.coleader.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nosae.coleader.MultiStateView
import com.nosae.coleader.R
import com.nosae.coleader.base.ScaleTab

/**
 * Create by NOSAE on 2020/4/22
 */

@BindingAdapter("level")
fun TextView.level(level: Int) {
    text = when(level) {
        0 -> "紧急"
        1 -> "次紧急"
        2 -> "一般紧急"
        3 -> "不紧急"
        else -> "无所谓"
    }
}

@BindingAdapter("imageUrl", "fadeLoad", requireAll = false)
fun ImageView.imageUrl(url: String?, fadeLoad: Boolean = false) {
    Glide.with(this)
        .load(url)
        .also { if (fadeLoad)
            it.transition(DrawableTransitionOptions.withCrossFade())
        }
        .error(R.drawable.test_pic)
        .into(this)
}

@BindingAdapter("tabIndex")
fun ScaleTab.tabIndex(index: Int) {
    if (getCurrentTab() == index)
        return
    setCurrentTab(index)
}

@InverseBindingAdapter(attribute = "tabIndex")
fun ScaleTab.getTabIndex() = getCurrentTab()

@BindingAdapter("tabIndexAttrChanged")
fun ScaleTab.bind(listener: InverseBindingListener) {
    setOnTabSelectedListener(object : ScaleTab.OnTabSelectedListener {
        override fun onSelected(index: Int, tabText: String) {
            listener.onChange()
        }

        override fun onUnselected(index: Int, tabText: String) {}
    })

}

@BindingAdapter("isLoading")
fun MultiStateView.isLoading(isLoading: Boolean) {
    if (isLoading) {
        showLoading()
    } else if (getState() == MultiStateView.STATE_LOADING) {
        showContent()
    }
}

@BindingAdapter("onRetry")
fun MultiStateView.onRetry(l: ()->Unit) {
    setTryOnErrorListener {
        l()
    }
}

@BindingAdapter("gender")
fun TextView.gender(value: Int) {
    val newVal = value.gender
    if (text == value.gender)
        return
    text = newVal
}

@InverseBindingAdapter(attribute = "gender")
fun TextView.getGender() = text.toString().gender

@BindingAdapter("genderAttrChanged")
fun TextView.bind(listener: InverseBindingListener) {
    addTextChangedListener {
        listener.onChange()
    }
}

private val Int.gender
    get() = when(this) {
        1 -> "男"
        2 -> "女"
        else -> "保密"
    }

private val String.gender
    get() = when(this) {
        "男" -> 1
        "女" -> 2
        else -> 0
    }