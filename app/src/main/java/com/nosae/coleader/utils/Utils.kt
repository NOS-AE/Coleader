package com.nosae.coleader.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nosae.coleader.BuildConfig
import com.nosae.coleader.MultiStateView
import com.nosae.coleader.MyApplication
import com.nosae.coleader.R
import org.greenrobot.eventbus.EventBus
import java.io.Closeable

/**
 * Create by NOSAE on 2020/4/22
 */
typealias EmptyCallback = ()->Unit

private val outMetrics by lazy {
    val displayMetrics = DisplayMetrics()
    val windowManager = MyApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    displayMetrics
}

val Number.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), outMetrics).toInt()
val Number.sp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), outMetrics).toInt()
val Number.dpf get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), outMetrics)
val Number.spf get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), outMetrics)
val deviceWidth get() = outMetrics.widthPixels

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun Activity.setTransparentStatusBar(isLight: Boolean) {
    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (isLight)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    else
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    window.statusBarColor = Color.TRANSPARENT
}

fun Activity.setStatusBarIcon(isDark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        var vis = decorView.systemUiVisibility
        vis = if (isDark) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}

inline fun <reified T: Activity> Activity.startActivity(bundle: Bundle? = null, options: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent, options)
}

inline fun <reified T: Activity> Fragment.startActivity(bundle: Bundle? = null, options: Bundle? = null) {
    val intent = Intent(context, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent, options)
}

inline fun <reified T: Activity> Context?.startActivity(bundle: Bundle? = null, options: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    this?.startActivity(intent, options)
}

inline fun <reified T: Activity> Activity.startActivityForResult(bundle: Bundle? = null, requestCode: Int, options: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T: Activity> Fragment.startActivityForResult(bundle: Bundle? = null, requestCode: Int, options: Bundle? = null) {
    val intent = Intent(context, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode, options)
}

fun <T : Closeable, S: Closeable, R> use(c1: T, c2: S, f: (T, S) -> R) =
    c1.use { c2.use { f(c1, c2) } }

fun <T : Closeable, S: Closeable, U: Closeable, R> use(c1: T, c2: S, c3: U, f: (T, S, U) -> R) =
    c1.use { c2.use { c3.use { f(c1, c2, c3) } } }

fun toast(msg: String) = Toast.makeText(MyApplication.instance, msg, Toast.LENGTH_SHORT).show()

fun debug(msg: String) {
    if (!BuildConfig.DEBUG)
        return
    Log.d("Coleader-DEBUG", msg)
}

fun error(msg: String) {

}

fun View.visible() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE)
        visibility = View.INVISIBLE
}

fun View.gone() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.test_pic)
        .centerCrop()
        .into(this)
}

fun ImageView.load(uri: Uri?) {
    Glide.with(this)
        .load(uri)
        .error(R.drawable.test_pic)
        .into(this)
}

fun ImageView.load(res: Int) {
    Glide.with(this)
        .load(res)
        .error(R.drawable.test_pic)
        .into(this)
}

fun <T, U: RecyclerView.ViewHolder>ListAdapter<T, U>.submitList(stateView: MultiStateView, list: List<T>?) {
    when {
        list == null -> {
            stateView.showError()
        }
        list.isEmpty() -> {
            stateView.showEmpty()
        }
        else -> {
            stateView.showContent()
            submitList(list)
        }
    }
}

fun <T, U: RecyclerView.ViewHolder>RecyclerView.Adapter<U>.notifyDataSetChange(stateView: MultiStateView, list: List<T>?) {
    when {
        list == null -> {
            stateView.showError()
        }
        list.isEmpty() -> {
            stateView.showEmpty()
        }
        else -> {
            stateView.showContent()
            notifyDataSetChanged()
        }
    }
}

@Suppress("DEPRECATION")
fun isNetworkAvailable(): Boolean {
    val cm = MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager? ?: return false
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
    } else {
        val networkInfo = cm.activeNetworkInfo
        if(networkInfo == null)
            false
        else
            networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE
    }
}

fun View.setOnDelayClickListener(delay: Long = 1000L, action: (View)->Unit) {
    var last = 0L
    setOnClickListener {
        val cur = System.currentTimeMillis()
        if (cur - last > delay) {
            action(this)
            last = cur
        }
    }
}

@MainThread
fun <U> MutableLiveData<ArrayList<U>>.add(item: U) {
    val newList = value ?: return
    newList.add(item)
    value = newList
}

fun <U> MutableLiveData<ArrayList<U>>.postAdd(item: U) {
    val newList = value ?: return
    newList.add(item)
    postValue(newList)
}

@MainThread
fun <U> MutableLiveData<ArrayList<U>>.remove(item: U) {
    val newList = value ?: return
    newList.remove(item)
    value = newList
}

fun <U> MutableLiveData<ArrayList<U>>.postRemove(item: U) {
    val newList = value ?: return
    newList.remove(item)
    postValue(newList)
}

@MainThread
fun <U> MutableLiveData<ArrayList<U>>.removeAt(pos: Int) {
    val newList = value ?: return
    newList.removeAt(pos)
    value = newList
}

fun <U> MutableLiveData<ArrayList<U>>.postRemoveAt(pos: Int) {
    val newList = value ?: return
    newList.removeAt(pos)
    postValue(newList)
}

fun registerBus(subscriber: Any) = EventBus.getDefault().register(subscriber)
fun postEvent(event: Any) = EventBus.getDefault().post(event)
fun postStickyEvent(event: Any) = EventBus.getDefault().postSticky(event)
fun removeStickyEvent(event: Any) = EventBus.getDefault().removeStickyEvent(event)
fun unregisterBus(subscriber: Any) = EventBus.getDefault().unregister(subscriber)