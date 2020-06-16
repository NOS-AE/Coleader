package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Friend
import com.nosae.coleader.repository.TaskRepo
import com.nosae.coleader.repository.TeamRepo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Create by NOSAE on 2020/6/10
 */

class EditTaskViewModel(
    private val repo: TaskRepo,
    private val teamRepo: TeamRepo
) : BaseViewModel() {

    var teamId = -1L
    var taskId = -1L
    val type = MutableLiveData(0)
    val memberResMutable = MutableLiveData<ArrayList<Friend>>(arrayListOf())
    val memberRes = SingleLiveEvent<ArrayList<Friend>>()
    private val _deleteRes = SingleLiveEvent<String>()
    val deleteRes: LiveData<String?> = _deleteRes
    private val _res = SingleLiveEvent<String>()
    val res: LiveData<String?> = _res

    val intro = MutableLiveData<String>()
    val startAt = MutableLiveData<String>()
    val endAt = MutableLiveData<String>()
    val remark = MutableLiveData<String>("")
    val content = MutableLiveData<String>()

    fun getMember() = launchNetwork {
        val res = teamRepo.getTeamMembers(teamId)
        if (res != null && res.errno == "0") {
            memberRes.postValue(ArrayList(res.data.rows))
        } else {
            memberRes.postCall()
        }
    }

    fun create() = launchNetwork {
        val participants = memberResMutable.value!!.map {
            it.id
        }
        val res = repo.createTask(
            teamId,
            intro.value!!,
            content.value!!,
            remark.value!!,
            startAt.value!!,
            endAt.value!!,
            participants
        )
        if (res != null && res.errno == "0") {
            _res.postCall()
        } else {
            _res.postValue("创建失败")
        }
    }

    fun update() = launchNetwork {
        val participants = memberResMutable.value!!.map {
            it.id
        }
        val res = repo.updateTask(
            teamId,
            intro.value!!,
            content.value!!,
            remark.value!!,
            startAt.value!!,
            endAt.value!!,
            participants,
            taskId
        )
        if (res != null && res.errno == "0") {
            _res.postCall()
        } else {
            _res.postValue("更新失败")
        }
    }

    fun delete() = launchNetwork {
        val res = repo.deleteTask(taskId)
        if (res != null && res.errno == "0") {
            _deleteRes.postCall()
        } else {
            _deleteRes.postValue("删除失败")
        }
    }

    private val tempCal = Calendar.getInstance().also {
        it[Calendar.SECOND] = 0
    }
    private val tempFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    fun formatDate(y: Int, M: Int, d: Int, H: Int, m: Int): String {
        tempCal[Calendar.YEAR] = y
        tempCal[Calendar.MONTH] = M
        tempCal[Calendar.DATE] = d
        tempCal[Calendar.HOUR_OF_DAY] = H
        tempCal[Calendar.MINUTE] = m
        return tempFormat.format(tempCal.time)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditTaskViewModel(TaskRepo(), TeamRepo()) as T
        }
    }
}