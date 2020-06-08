package com.nosae.coleader.base

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Create by NOSAE on 2020/4/22
 *
 * 显式setValue才被observe，避免由比如rotation引起
 */
class SingleLiveEvent<T>: MutableLiveData<T?>() {

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        super.observe(owner, Observer {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        value = null
    }

    fun postCall() {
        postValue(null)
    }
}