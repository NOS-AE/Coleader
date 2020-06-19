package com.nosae.coleader.viewmodels

import android.util.SparseArray
import androidx.core.util.set
import androidx.lifecycle.*
import com.nosae.coleader.adapters.FileItem
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Task
import com.nosae.coleader.repository.TaskRepo
import com.nosae.coleader.utils.add
import com.nosae.coleader.utils.remove

/**
 * Create by NOSAE on 2020/6/16
 */

class TaskViewModel(private val repo: TaskRepo) : BaseViewModel() {

    val task = MutableLiveData<Task>()
    lateinit var fileMap: SparseArray<Task.File>
    lateinit var indexMap: SparseArray<Int>

    val downloadFile = task.switchMap {
        if (it.status == 0) {
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
                    index++
                }
            }
            MutableLiveData<List<FileItem>>().apply {
                postValue(res)
            }
        }
    }
    val uploadFile = MutableLiveData<ArrayList<FileItem>>()

    private val _deleteRes = SingleLiveEvent<String>()
    val deleteRes: LiveData<String?> = _deleteRes

    private val _submitRes = SingleLiveEvent<String>()
    val submitRes: LiveData<String?> = _submitRes

    fun download(file: FileItem) {
        val name = file.name
        val url = file.path
    }

    fun deleteFile(file: FileItem) {
        uploadFile.remove(file)
    }

    fun addFile(file: FileItem) {
        uploadFile.add(file)
    }

    fun submitTask() = launchNetwork {

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