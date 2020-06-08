package com.nosae.coleader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nosae.coleader.utils.isNetworkAvailable
import com.nosae.coleader.utils.toast
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Create by NOSAE on 2020/5/5
 */
abstract class BaseViewModel: ViewModel() {

    val isLoading by lazy { MutableLiveData(false) }

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context, start, block)

    fun launchNetwork(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        noNetWork: (() -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit

    ): Job? {
        if (!isNetworkAvailable()) {
            toast("网络不可用")
            noNetWork?.invoke()
            return null
        }
        return viewModelScope.launch(context, start, block)
    }

    fun <T>network(
        block: () -> T
    ): T {
        if (!isNetworkAvailable()) {
            toast("网络不可用")
        }
        return block()
    }
}