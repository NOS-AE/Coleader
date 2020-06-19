package com.nosae.coleader

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import cn.jpush.android.ups.JPushUPSManager
import com.nosae.coleader.config.JPush_APP_KEY
import com.nosae.coleader.config.setAlias
import com.nosae.coleader.repository.SharedPref
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.startActivity
import io.socket.client.Socket
import java.util.*

/**
 * Create by NOSAE on 2020/4/22
 */
class MyApplication: Application() {

    companion object {
        lateinit var instance: MyApplication
            private set
        lateinit var socket: Socket

        private val list by lazy {
            LinkedList<Activity>()
        }
        private var currentActivity: Activity? = null

        fun startLogin() {
            SharedPref.loginAccount = ""
            SharedPref.userPassword = ""
            currentActivity?.startActivity<LoginActivity>()
            list.forEach {
                it.finish()
            }
            while (list.isNotEmpty()) {
                list.remove()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val mainProcessName = getCurrentProcessName()
        registerActivity(mainProcessName)
        initJPush()
    }

    private fun initJPush() {
        JPushUPSManager.registerToken(this, JPush_APP_KEY, null, null) {
            debug("JPush registerToken ${it.returnCode}")
            // JPush别名
            if (it.returnCode == 0) {
                setAlias("initJPush alias")
            }
        }
    }

    private fun registerActivity(processName: String) {
        if (packageName != processName)
            return
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityDestroyed(activity: Activity) {
                list.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                list.add(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }
        })
    }

    private fun getCurrentProcessName(): String {
        var res = ""
        val pid = android.os.Process.myPid()
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.runningAppProcesses.forEach {
            if (it.pid == pid) {
                res = it.processName
            }
        }
        return res
    }
}