package com.nosae.coleader

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.*
import androidx.lifecycle.lifecycleScope
import com.github.lzyzsd.jsbridge.BridgeWebView
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.FormEvent
import com.nosae.coleader.data.RetrofitHelper
import com.nosae.coleader.databinding.ActivityWebBinding
import com.nosae.coleader.utils.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Bus
class WebActivity : BaseActivity<ActivityWebBinding>() {

    private var type = -1

    override fun getLayoutId(): Int = R.layout.activity_web

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews(b: ActivityWebBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        webView.settings.run {
            javaScriptEnabled = true
            useWideViewPort = true
            javaScriptCanOpenWindowsAutomatically = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            domStorageEnabled = true

        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100)
                    pb.visible()
                pb.progress = newProgress
                if (newProgress == 100)
                    pb.gone()
            }
        }
        val isCreate = intent.getBooleanExtra("create", false)
        val teamId = intent.getLongExtra("teamId", -1)
        type = intent.getIntExtra("type", -1)
        setCenterTitle(when (type) {
            WRITE -> "填写问卷"
            RESULT -> "问卷填写详情"
            else -> "创建问卷"
        })
        if (isCreate) {
            if (teamId == -1L) {
                toast("团队ID未知")
                finish()
            }
            webView.createForm {
                NewFormBean(getToken(), teamId).toJson().also {
                    debug(it)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetForm(e: FormEvent) {
        removeStickyEvent(e)
        val form = e.form
        if (type == WRITE) {
            webView.writeForm {
                WriteFormBean(getToken(), form.id, form.teamId).toJson().also {
                    debug(it)
                }
            }
        } else {
            webView.showResult {
                ResultFormBean(getToken(), form.teamId, form.params).toJson().also {
                    debug(it)
                }
            }
        }
    }

    private fun BridgeWebView.createForm(data: suspend () -> String) {
        registerHandler("createForm") { _, cb ->
            lifecycleScope.launch {
                cb.onCallBack(data())
            }
        }
        loadUrl("http://39.108.117.52:8080/questionnaire/create")
    }

    private fun BridgeWebView.writeForm(data: suspend () -> String) {
        registerHandler("writeForm") { _, cb ->
            lifecycleScope.launch {
                cb.onCallBack(data())
            }
        }
        loadUrl("http://39.108.117.52:8080/questionnaire/show")
    }

    private fun BridgeWebView.showResult(data: suspend () -> String) {
        registerHandler("showResult") { _, cb ->
            lifecycleScope.launch {
                cb.onCallBack(data())
            }
        }
        loadUrl("http://39.108.117.52:8080/questionnaire/result")
    }

    private suspend fun getToken() = withIO {
        try {
            RetrofitHelper.getToken()
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        fun start(ctx: Context?, teamId: Long) {
            ctx.startActivity<WebActivity>(Bundle().apply {
                putBoolean("create", true)
                putLong("teamId", teamId)
            })
        }

        fun start(ctx: Context?, type: Int) {
            ctx.startActivity<WebActivity>(Bundle().apply {
                putInt("type", type)
            })
        }

        const val WRITE = 0
        const val RESULT = 1
    }

    private object MoshiHelper {
        private val moshi = Moshi.Builder().build()
        val newFormAdapter: JsonAdapter<NewFormBean> by lazy { moshi.adapter(NewFormBean::class.java) }
        val writeFormAdapter: JsonAdapter<WriteFormBean> by lazy { moshi.adapter(WriteFormBean::class.java) }
        val resultFormAdapter: JsonAdapter<ResultFormBean> by lazy { moshi.adapter(ResultFormBean::class.java) }
    }

    @JsonClass(generateAdapter = true)
    data class NewFormBean(
        var token: String,
        var teamId: Long
    ) {
        fun toJson(): String = MoshiHelper.newFormAdapter.toJson(this)
    }

    @JsonClass(generateAdapter = true)
    data class WriteFormBean(
        var token: String,
        var formId: Long,
        var teamId: Long
    ) {
        fun toJson(): String = MoshiHelper.writeFormAdapter.toJson(this)
    }

    @JsonClass(generateAdapter = true)
    data class ResultFormBean(
        var token: String,
        var teamId: Long,
        var params: String
    ) {
        fun toJson(): String = MoshiHelper.resultFormAdapter.toJson(this)
    }

}