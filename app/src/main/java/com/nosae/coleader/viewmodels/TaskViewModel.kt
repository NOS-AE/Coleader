package com.nosae.coleader.viewmodels

import android.util.SparseArray
import androidx.core.util.set
import androidx.lifecycle.*
import com.nosae.coleader.MyApplication
import com.nosae.coleader.adapters.FileItem
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Task
import com.nosae.coleader.repository.TaskRepo
import com.nosae.coleader.utils.add
import com.nosae.coleader.utils.debug
import com.nosae.coleader.utils.remove
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Create by NOSAE on 2020/6/16
 */

class TaskViewModel(private val repo: TaskRepo) : BaseViewModel() {

    val task = MutableLiveData<Task>()
    lateinit var fileMap: SparseArray<Task.File>
    lateinit var indexMap: SparseArray<Int>

    val downloadFile = task.switchMap {
        if (!it.isAdmin) {
            MutableLiveData()
        } else {
            fileMap = SparseArray()
            indexMap = SparseArray()
            val res = ArrayList<FileItem>()
            var index = 0
            for (userFile in it.files) {
                if (userFile.fileName == null) continue
                val filenames = userFile.fileName!!
                val urls = userFile.url!!
                for (i in filenames.indices) {
                    res += FileItem(
                        filenames[i],
                        urls[i]
                    )
                    fileMap[index] = userFile
                    indexMap[index] = it.files.indexOf(userFile)
                    debug("indexMap $index")
                    index++
                }
            }
            MutableLiveData<List<FileItem>>().apply {
                postValue(res)
            }
        }
    }
    val uploadFile = MutableLiveData<ArrayList<FileItem>>()
    val title = MutableLiveData<String>("")
    val content = MutableLiveData<String>("")

    private val _deleteRes = SingleLiveEvent<String>()
    val deleteRes: LiveData<String?> = _deleteRes

    private val _submitRes = SingleLiveEvent<String>()
    val submitRes: LiveData<String?> = _submitRes

    private val _downloadRes = SingleLiveEvent<String>()
    val downloadRes: LiveData<String?> = _downloadRes

    fun download(fileItem: FileItem) = launchNetwork {
        val name = fileItem.name
        val url = "http://yjcxlr.cn:3000${fileItem.path}"
        val response = repo.downloadFile(url)
        if (response == null) {
            _downloadRes.postValue("下载失败")
            return@launchNetwork
        }
        val body = response.body()
        if (body == null) {
            _downloadRes.postValue("下载失败")
            return@launchNetwork
        }
        try {
            val file = File(MyApplication.instance.getExternalFilesDir("download"), fileItem.name)
            val output = file.outputStream()
            val input = body.byteStream()
            val buffer = ByteArray(4096)
            var size: Int
            while ((input.read(buffer).apply { size = this }) != -1) {
                output.write(buffer, 0, size)
            }
            output.flush()
            input.close()
            output.close()
            fileItem.downloaded = true
            fileItem.path = file.absolutePath
            debug("下载成功 ${fileItem.path}")
            _downloadRes.postCall()
        } catch (e: Exception) {
            e.printStackTrace()
            _downloadRes.postValue("下载失败")
        }
    }

    fun deleteFile(file: FileItem) {
        uploadFile.remove(file)
    }

    fun addFile(file: FileItem) {
        uploadFile.add(file)
    }

    fun submitTask() = launchNetwork {
        val fileUrls = ArrayList<String>()
        val fileNames = ArrayList<String>()
        uploadFile.value!!.forEach {
            val file = File(it.path)
            val fileBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)
            val uploadRes = repo.uploadFile("task", filePart)
            if (uploadRes == null || uploadRes.errno != "0") {
                _submitRes.postValue("提交失败")
                return@launchNetwork
            }
            fileUrls += uploadRes.data.path
            fileNames += file.name
        }
        val task = task.value!!
        val res = repo.submitTask(
            task.id,
            task.teamId,
            title.value!!,
            content.value!!,
            fileUrls,
            fileNames
        )
        if (res != null && res.errno == "0") {
            _submitRes.postCall()
        } else {
            _submitRes.postValue("提交失败")
        }
    }

    fun deleteTask() = launchNetwork {
        val res = repo.deleteTask(task.value!!.id)
        if (res != null && res.errno == "0") {
            _deleteRes.postCall()
        } else {
            _deleteRes.postValue("删除失败")
        }
    }

    class Factory: ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TaskViewModel(TaskRepo()) as T
        }
    }
}