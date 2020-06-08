package com.nosae.coleader.config

import android.content.Context
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.service.JCommonService
import cn.jpush.android.service.JPushMessageReceiver
import com.nosae.coleader.MyApplication
import com.nosae.coleader.data.TempData
import com.nosae.coleader.repository.SharedPref
import com.nosae.coleader.utils.debug

/**
 * Create by NOSAE on 2020/5/12
 */

const val JPush_APP_KEY = "39dbbe425e73a17efa98db39"

class JiGuangService: JCommonService()

class JiGuangReceiver: JPushMessageReceiver() {
    override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onAliasOperatorResult(p0, p1)
        p1?.let {
            debug("onAliasOperatorResult ${it.errorCode} ${it.sequence} ${it.alias}")
            if (it.errorCode == 0) {
                SharedPref.hasSetAlias = true
            }
        }
    }
}

// JPush别名
fun setAlias(debugInfo: String? = null) {
    if (SharedPref.hasSetAlias) {
        return
    }
    if (TempData.canSetAlias) {
        debugInfo?.let { debug(it) }

        val id = TempData.userInfo?.data?.id ?: return
        JPushInterface.setAlias(
            MyApplication.instance,
            System.currentTimeMillis().toInt(),
            id.toString()
        )
    }
    TempData.canSetAlias = !TempData.canSetAlias
}