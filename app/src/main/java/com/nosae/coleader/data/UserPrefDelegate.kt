package com.nosae.coleader.data

import android.content.Context
import android.content.SharedPreferences
import com.nosae.coleader.MyApplication

/**
 * Create by NOSAE on 2020/4/22
 */
class UserPrefDelegate<T>(default: T): PrefDelegate<T>(default) {
    override fun getSharedPreferences(): SharedPreferences {
        return spf
    }

    companion object {
        private val spf by lazy {
            MyApplication.instance.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        }
    }

}