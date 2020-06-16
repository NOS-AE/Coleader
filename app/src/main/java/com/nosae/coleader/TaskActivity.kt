package com.nosae.coleader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.databinding.ActivityTaskBinding
import com.nosae.coleader.utils.FileUtil
import com.nosae.coleader.utils.snack
import com.nosae.coleader.utils.toast
import java.io.File

class TaskActivity : BaseActivity<ActivityTaskBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_task
    }

    override fun initViews(b: ActivityTaskBinding, savedInstanceState: Bundle?) {
        b.fabFile.setOnClickListener {
            pickFile()
        }
        b.fabMd.setOnClickListener {

        }
    }

    private fun pickFile() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 0)
        }.onDeclined {
            if (it.hasForeverDenied()) {
                snack(mBinding.coordinator, "读文件权限已被禁止", "去开启") { _ ->
                    it.goToSettings()
                }
            } else {
                snack(mBinding.coordinator, "读文件权限已被禁止", "重试") { _ ->
                    it.askAgain()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == 0) {
            try {
                val filePath = FileUtil.getPath(this, data!!.data!!)!!
                val file = File(filePath)
            } catch (e: Exception) {
                e.printStackTrace()
                toast("获取文件失败")
            }

        }
    }

}
