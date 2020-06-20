package com.nosae.coleader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.nosae.coleader.adapters.FileAdapter
import com.nosae.coleader.adapters.FileItem
import com.nosae.coleader.base.BaseActivity
import com.nosae.coleader.data.TaskEvent
import com.nosae.coleader.data.UpdateTaskEvent
import com.nosae.coleader.databinding.ActivityTaskBinding
import com.nosae.coleader.utils.*
import com.nosae.coleader.view.HeaderDecoration
import com.nosae.coleader.viewmodels.TaskViewModel
import kotlinx.android.synthetic.main.activity_task.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

@Bus
class TaskActivity : BaseActivity<ActivityTaskBinding>() {

    private val viewModel by viewModels<TaskViewModel> {
        TaskViewModel.Factory()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_task
    }

    override fun initViews(b: ActivityTaskBinding, savedInstanceState: Bundle?) {
        bindToolbar("")
        setCenterTitle("任务详情")
        b.viewModel = viewModel
        b.fabFile.setOnClickListener {
            pickFile()
        }
        fab_file.setOnClickListener {
            pickFile()
        }
        tv_name.text = "啊啊啊啊啊啊"

        b.fabMd.setOnClickListener {
            MarkdownActivity.start(this)
        }
        b.rv.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val adapter = FileAdapter({
            viewModel.deleteFile(it)
        }, {
            viewModel.download(it)
        }) {
            val intent = Intent(Intent.ACTION_SEND)
            val uri = Uri.parse(it.path)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "分享文件"))
        }
        b.rv.adapter = adapter

        val decoration = HeaderDecoration(object : HeaderDecoration.Callback {
            override fun getGroupIndex(pos: Int): Int {
                return viewModel.indexMap[pos]
            }

            override fun getGroupTitle(pos: Int): String {
                return viewModel.fileMap[pos].nickname + "(${viewModel.fileMap[pos].username})"
            }
        })

        viewModel.downloadFile.observe(this) {
            adapter.status = 1
            b.rv.addItemDecoration(decoration)
            adapter.submitList(b.multiStateView, it)
            // adapter.notifyDataSetChange(multiStateView, it)
        }
        viewModel.uploadFile.observe(this) {
            adapter.status = 0
            b.rv.removeItemDecoration(decoration)
            adapter.submitList(b.multiStateView, it)
            adapter.notifyDataSetChanged()
        }
        viewModel.downloadRes.observe(this) {
            if (it == null) {
                adapter.notifyDataSetChanged()
            } else {
                toast(it)
            }
        }
        viewModel.deleteRes.observe(this) {
            if (it == null) {
                postEvent(UpdateTaskEvent())
                finish()
            } else {
                toast(it)
            }
        }
        viewModel.submitRes.observe(this) {
            if (it == null) {
                postEvent(UpdateTaskEvent())
                finish()
            } else {
                toast(it)
            }
        }
    }

    private fun pickFile() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 0)
        }.onDeclined {
            if (it.hasForeverDenied()) {
                snack(mBinding.root, "读文件权限已被禁止", "去开启") { _ ->
                    it.goToSettings()
                }
            } else {
                snack(mBinding.root, "读文件权限已被禁止", "重试") { _ ->
                    it.askAgain()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetTask(e: TaskEvent) {
        // debug("onGetTask")
        removeStickyEvent(e)
        val task = e.task
        viewModel.task.value = task
        viewModel.uploadFile.value = ArrayList()
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
                viewModel.addFile(
                    FileItem(
                        file.name,
                        filePath
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                toast("获取文件失败")
            }
        } else if (requestCode == MarkdownActivity.REQUEST_MD) {
            val mdPath = data!!.getStringExtra("md_path")
            val file = File(mdPath!!)
            viewModel.addFile(
                FileItem(
                    file.name,
                    file.path
                )
            )
        }
    }
}