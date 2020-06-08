package com.nosae.coleader

import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.ByteString
import org.junit.Test

import org.junit.Assert.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

    }

    fun mockServer(): MockWebServer {
        val server = MockWebServer()
        server.enqueue(MockResponse().withWebSocketUpgrade(object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                println("onClosed")
                println("code $code")
                println("reason $reason")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                println("onClosing")
                println("code $code")
                println("reason $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                println("onFailure")
                println("e: $t")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                println("onMessage $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                println("onOpen")
            }
        }))
        return server
    }

    @Test
    fun mockClient() {
        val server = mockServer()
        val host = server.hostName
        val port = server.port

        println("server $host:$port")

        val request = Request.Builder()
            .url("ws://106.15.192.62:3000/")
            // .addHeader("Origin", "http://106.15.192.62:3000/")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXNlcm5hbWUiOiIxMjMiLCJlbWFpbCI6IjI0NzU5NDU4NjhAcXEuY29tIiwiaWF0IjoxNTg5MDMyMTUxLCJleHAiOjE1ODkwMzU3NTF9.TKxUsaMN722vVnoNAYDEsBEGkRygyFyUAsVoqyg979A")
            // .url("ws://$host:$port/")
            .build()
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
            .newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                println("onClosed")
                println("code $code")
                println("reason $reason")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                println("onClosing")
                println("code $code")
                println("reason $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                println("onFailure")
                println("e: $t")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                println("onMessage $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                println("onOpen")
                webSocket.send("hello")
            }
        })

        Thread.sleep(10000)
    }

    @Test
    fun f() {
        val opt = IO.Options()
        opt.transports = arrayOf(io.socket.engineio.client.transports.WebSocket.NAME)
        val socket = IO.socket("https://socket-io-chat.now.sh/", opt)

        socket.connect()
            .on("ferret") {
                println("onChat")
                it.forEach {
                    println(it.toString())
                }
            }.on(Socket.EVENT_ERROR) {
                println("onError")
                it.forEach {
                    println(it.toString())
                }
            }.on(Socket.EVENT_CONNECT_ERROR) {
                println("onConnectError")
                it.forEach {
                    if (it is Exception) {
                        it.printStackTrace()
                        println(it.toString())
                    }
                }
            }.on(Socket.EVENT_CONNECTING) {
                println("Connection")
            }.on(Socket.EVENT_CONNECT) {
                println("Connect")
            }
            .emit("ferret", "你好", "世界", "操你妈")

        Thread.sleep(10000)
    }
}
