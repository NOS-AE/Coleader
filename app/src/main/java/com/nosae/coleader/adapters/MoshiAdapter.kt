package com.nosae.coleader.adapters

import com.squareup.moshi.*
import com.squareup.moshi.internal.Util
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by NOSAE on 2020/5/5
 */

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class HostPrefix

class HostPrefixFactory : JsonAdapter.Factory {

    private val annClass = HostPrefix::class.java

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (annotations isAnnotationPresent annClass) {
            return Adapter
        }
        return null
    }

    private object Adapter : JsonAdapter<String>() {
        override fun fromJson(reader: JsonReader): String? {
            return when (reader.peek()) {
                JsonReader.Token.NULL -> {
                    reader.nextNull<Any>()
                    ""
                }
                else -> "http://yjcxlr.cn:3000" + reader.nextString()
            }
        }

        override fun toJson(writer: JsonWriter, value: String?) {
            writer.value(value)
        }

    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToString

class NullToStringFactory: JsonAdapter.Factory {

    private val annClass = NullToString::class.java

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (annotations isAnnotationPresent annClass) {
            return Adapter
        }
        return null
    }

    private object Adapter: JsonAdapter<String>() {
        override fun fromJson(reader: JsonReader): String? {
            return when(reader.peek()) {
                JsonReader.Token.NULL -> {
                    reader.nextNull<Any>()
                    ""
                } else -> reader.nextString()
            }
        }

        override fun toJson(writer: JsonWriter, value: String?) {
            writer.value(value)
        }

    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToInt

class NullToIntFactory: JsonAdapter.Factory {

    private val annClass = NullToInt::class.java

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (annotations isAnnotationPresent annClass) {
            return Adapter
        }
        return null
    }

    private object Adapter: JsonAdapter<Int>() {
        override fun fromJson(reader: JsonReader): Int? {
            return when(reader.peek()) {
                JsonReader.Token.NULL -> {
                    reader.nextNull<Any>()
                    0
                } else -> reader.nextInt()
            }
        }

        override fun toJson(writer: JsonWriter, value: Int?) {
            writer.value(value)
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class GMTToFormat(val field: Int = DATE_TIME) {
    companion object {
        const val DATE = 0
        const val TIME = 1
        const val DATE_TIME = 2
    }
}

private val gmtFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA)
class GMTToFormatFactory: JsonAdapter.Factory {

    private val annClass = GMTToFormat::class.java

    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        val ann = annotations.find { it is GMTToFormat } as? GMTToFormat
        return if (ann != null) {
            when(ann.field) {
                GMTToFormat.DATE -> DateAdapter
                GMTToFormat.TIME -> TimeAdapter
                else -> DateTimeAdapter
            }
        } else {
            null
        }
    }

    private object DateTimeAdapter: JsonAdapter<String>() {
        private val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        override fun fromJson(reader: JsonReader): String? {
            return when(reader.peek()) {
                JsonReader.Token.NULL -> {
                    reader.nextNull<String>()
                } else -> {
                    val gmt = reader.nextString()
                    gmtFormat.parse(gmt)?.let {
                        format.format(it)
                    } ?: gmt
                }
            }
        }

        override fun toJson(writer: JsonWriter, value: String?) {
            writer.value(value)
        }

    }

    private object DateAdapter: JsonAdapter<String>() {
        private val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        override fun fromJson(reader: JsonReader): String? {
            return when(reader.peek()) {
                JsonReader.Token.NULL -> {
                    reader.nextNull<String>()
                } else -> {
                    val gmt = reader.nextString()
                    gmtFormat.parse(gmt)?.let {
                        format.format(it)
                    } ?: gmt
                }
            }
        }

        override fun toJson(writer: JsonWriter, value: String?) {
            writer.value(value)
        }

    }

    private object TimeAdapter: JsonAdapter<String>() {
        private val format = SimpleDateFormat("HH:mm", Locale.CHINA)
        override fun fromJson(reader: JsonReader): String? {
            return when(reader.peek()) {
                JsonReader.Token.NULL -> {
                    reader.nextNull<String>()
                } else -> {
                    val gmt = reader.nextString()
                    gmtFormat.parse(gmt)?.let {
                        format.format(it)
                    } ?: gmt
                }
            }
        }

        override fun toJson(writer: JsonWriter, value: String?) {
            writer.value(value)
        }

    }

}

// Code generated by moshi-kotlin-codegen
// TODO 写一个toJson skipNull codegen


private infix fun Set<Annotation>.isAnnotationPresent(clazz: Class<out Annotation>): Boolean
    = Util.isAnnotationPresent(this, clazz)