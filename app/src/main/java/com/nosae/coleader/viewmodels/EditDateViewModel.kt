package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.EditDateActivity
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.repository.EditDateRepo
import com.nosae.coleader.utils.add
import com.nosae.coleader.utils.removeAt
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by NOSAE on 2020/5/28
 */
class EditDateViewModel(
    private val repo: EditDateRepo
): BaseViewModel() {
    val intro = MutableLiveData("")
    val details = MutableLiveData("")
    val startAt = MutableLiveData("")
    val endAt = MutableLiveData("")
    val level = MutableLiveData(0)
    val tags = MutableLiveData(arrayListOf<String>())
    var type = MutableLiveData(0)
    var teamId = 0L
    var scheduleId = 0L

    private val _dateRes = SingleLiveEvent<String>()
    val dateRes: LiveData<String?> = _dateRes

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

    fun addTag(tag: String) {
        val toAdd = tag.trim()
        if (tag.isEmpty()) return
        tags.add(toAdd)
    }

    fun removeTag(pos: Int) {
        tags.removeAt(pos)
    }

    fun submit() = launchNetwork {
        if (type.value!! == EditDateActivity.CREATE) {
            val res = repo.createDate(
                teamId,
                startAt.value!!,
                endAt.value!!,
                intro.value!!,
                details.value!!,
                tags.value!!,
                level.value!!
            )
            if (res != null && res.errno == "0") {
                _dateRes.postCall()
            } else {
                _dateRes.postValue("创建失败")
            }
        } else {
            val res = repo.updateDate(
                teamId,
                startAt.value!!,
                endAt.value!!,
                intro.value!!,
                details.value!!,
                tags.value!!,
                level.value!!,
                scheduleId
            )
            if (res != null && res.errno == "0") {
                _dateRes.postCall()
            } else {
                _dateRes.postValue("修改失败")
            }
        }
    }

    fun delete() = launchNetwork {
        val res = repo.deleteDate(teamId, scheduleId)
        if (res != null && res.errno == "0") {
            _dateRes.postCall()
        } else {
            _dateRes.postValue("删除失败")
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditDateViewModel(EditDateRepo()) as T
        }
    }
}