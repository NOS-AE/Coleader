package com.nosae.coleader.data

import com.nosae.coleader.MyApplication
import com.nosae.coleader.adapters.GMTToFormatFactory
import com.nosae.coleader.adapters.NullToIntFactory
import com.nosae.coleader.adapters.NullToStringFactory
import com.nosae.coleader.repository.SharedPref
import com.nosae.coleader.utils.debug
import com.squareup.moshi.Moshi
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Create by NOSAE on 2020/5/5
 */
object RetrofitHelper {

    private val client by lazy { OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .dispatcher(Dispatcher().apply {
            maxRequests = 64
            maxRequestsPerHost = 32
        })
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor {
            // 为需要加token的请求加上token
            val request = it.request()
            val builder = request.newBuilder()
            request.header("Authorization")?.run {
                runBlocking {
                    builder.header("Authorization", getToken())
                }
            }
            it.proceed(builder.build())
        }.addInterceptor {
            val request = it.request()
            val url = request.url.toString()
            val res = it.proceed(request)
            if (url.contains("login") && res.isSuccessful) {
                // 记录登录时间
                debug("Login")
                TempData.loginTime = System.currentTimeMillis()
            }
            res
        }
        .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl("http://yjcxlr.cn:3000/")
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(NullToStringFactory())
                    .add(NullToIntFactory())
                    .add(GMTToFormatFactory())
                    .build()
            ))
            .build()
    }

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val teamService: TeamService by lazy { retrofit.create(TeamService::class.java) }
    val dateService: DateService by lazy { retrofit.create(DateService::class.java) }
    val punchService: PunchService by lazy { retrofit.create(PunchService::class.java) }

    private const val TOKEN_EXPIRED_TIME = 1000 * 60 * 10L
    private val tokenMutex = Mutex()
    @Synchronized
    suspend fun getToken(): String  = tokenMutex.withLock {
        if (System.currentTimeMillis() - TempData.loginTime > TOKEN_EXPIRED_TIME
            || TempData.token.isBlank()
        ) {
            val dto = LoginDto(SharedPref.loginAccount, SharedPref.userPassword)
            val loginRes = userService.login(dto)
            if (loginRes.data == null || loginRes.data?.token == null || loginRes.errno != "0") {
                MyApplication.startLogin()
                return@withLock ""
            }
            TempData.token = "Bearer ${loginRes.data!!.token}"
        }
        TempData.token
    }

    // DEMO code
    fun buildWS() {
        val socket = IO.socket("http://127.0.0.1:2851")
        socket.connect()
            .on("chat") {
                debug("onChat")
                it.forEach {
                    debug(it.toString())
                }
            }.on(Socket.EVENT_ERROR) {
                debug("onError")
                it.forEach {
                    debug(it.toString())
                }
            }.on(Socket.EVENT_CONNECT_ERROR) {
                debug("onConnectError")
                it.forEach {
                    debug(it.toString())
                }
            }.emit("chat", "你好", "世界")
    }

}