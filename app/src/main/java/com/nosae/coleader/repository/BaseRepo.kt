package com.nosae.coleader.repository

/**
 * Create by NOSAE on 2020/5/5
 */
abstract class BaseRepo {
    suspend fun <T>tryBlock(block: suspend ()->T): T? = try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}