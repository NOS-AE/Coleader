package com.nosae.coleader.utils

import org.greenrobot.eventbus.EventBus

/**
 * Create by NOSAE on 2020/6/15
 */
fun registerBus(subscriber: Any) = EventBus.getDefault().register(subscriber)
fun postEvent(event: Any) = EventBus.getDefault().post(event)
fun postStickyEvent(event: Any) = EventBus.getDefault().postSticky(event)
fun removeStickyEvent(event: Any) = EventBus.getDefault().removeStickyEvent(event)
fun unregisterBus(subscriber: Any) = EventBus.getDefault().unregister(subscriber)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Bus