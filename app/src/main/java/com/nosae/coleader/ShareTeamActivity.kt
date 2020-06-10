package com.nosae.coleader

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.content.FileProvider
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityShareTeamBinding
import com.nosae.coleader.utils.setOnDelayClickListener
import com.nosae.coleader.utils.setTransparentStatusBar
import com.nosae.coleader.utils.startActivity
import com.nosae.coleader.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ShareTeamActivity : BaseActivity<ActivityShareTeamBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_share_team
    }

    override fun initViews(b: ActivityShareTeamBinding, savedInstanceState: Bundle?) {
        setTransparentStatusBar(true)
        bindToolbar("")
        setCenterTitle("团队二维码")
        val url = intent.getStringExtra("url")
        val image = intent.getParcelableExtra<Bitmap>("image")!!
        b.ivQrcode.setImageBitmap(image)

        b.btnUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(intent, "邀请成员"))
        }
        b.btnQrcode.setOnDelayClickListener {
            val qrCodeFile = File(getExternalFilesDir("qrcode")!!.absolutePath, "qrcode${System.currentTimeMillis()}")
            if (!qrCodeFile.exists()) {
                launch(Dispatchers.IO) {
                    FileOutputStream(qrCodeFile).use {
                        image.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        it.flush()
                    }
                    withContext(Dispatchers.Main) {
                        if (!qrCodeFile.exists())
                            toast("分享二维码失败")
                        else {
                            shareCode(qrCodeFile)
                        }
                    }
                }
            } else {
                shareCode(qrCodeFile)
            }
        }
    }

    private fun shareCode(file: File) {
        val uri = FileProvider.getUriForFile(this@ShareTeamActivity, "com.nosae.coleader.fileprovider", file)
        startActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "image/*"
        })
    }

    companion object {
        fun start(context: Context?, url: String, image: Bitmap) {
            context.startActivity<ShareTeamActivity>(Bundle().apply {
                putString("url", url)
                putParcelable("image", image)
            })
        }
    }
}
