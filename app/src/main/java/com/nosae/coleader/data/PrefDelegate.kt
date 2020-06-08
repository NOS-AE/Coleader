package com.nosae.coleader.data

import android.content.SharedPreferences
import android.util.Base64
import com.nosae.coleader.utils.use
import java.io.*
import kotlin.reflect.KProperty

/**
 * Create by NOSAE on 2020/4/22
 */

@Suppress("UNCHECKED_CAST")
abstract class PrefDelegate<T>(private val default: T) {
    abstract fun getSharedPreferences(): SharedPreferences

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val key = property.name
        return with(getSharedPreferences()) {
            when(default) {
                is String -> getString(key, default)
                is Int -> getInt(key, default)
                is Boolean -> getBoolean(key, default)
                is Float -> getFloat(key, default)
                is Long -> getLong(key, default)
                is Serializable -> deserialize(getString(key, "")) ?: default
                else -> getString(key, null)
            } as T
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val key = property.name
        with(getSharedPreferences().edit()) {
            when(value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Serializable -> putString(key, serialize(value))
                else -> putString(key, null)
            }
            apply()
        }
    }

    /**
     * serialize Serializable using base64
     */
    private fun serialize(obj: T?): String? {
        if (obj == null)
            return null
        val baos = ByteArrayOutputStream()
        return use(baos, ObjectOutputStream(baos)) { c1, c2 ->
            c2.writeObject(obj)
            String(Base64.encode(c1.toByteArray(), Base64.DEFAULT))
        }
    }

    /**
     * deserialize using base64
     */
    private fun deserialize(str: String?): T? {
        if (str == null)
            return null
        val bais = ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT))
        return use(bais, ObjectInputStream(bais)) { c1, c2 ->
            c2.readObject() as T
        }
    }
}