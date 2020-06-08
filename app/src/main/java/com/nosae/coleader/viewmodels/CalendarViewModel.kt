package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haibin.calendarview.Calendar
import com.nosae.coleader.CalendarActivity
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Date
import com.nosae.coleader.repository.DateRepo

/**
 * Create by NOSAE on 2020/6/1
 */
class CalendarViewModel(
    private val repo: DateRepo
): BaseViewModel() {

    var type = MutableLiveData(0)
    var isAdmin = MutableLiveData(false)
    var teamId = 0L

    private val _dateRes = SingleLiveEvent<List<Date>>()
    val dateRes: LiveData<List<Date>?> = _dateRes

    private var calendar: Calendar? = null

    fun getDate(calendar: Calendar) {
        this.calendar = calendar
        isLoading.value = true
        launchNetwork {
            val res = when(type.value!!) {
                CalendarActivity.USER -> { repo.getTimeDate(calendar.toString()) }
                CalendarActivity.TEAM -> { repo.getTeamDate(teamId, calendar.toString()) }
                else -> null
            }
            if (res != null && res.errno == "0") {
                _dateRes.postValue(res.data.rows)
            } else {
                _dateRes.postCall()
            }
            isLoading.postValue(false)
        }
    }

    fun retry() {
        calendar?.let {
            getDate(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CalendarViewModel(DateRepo()) as T
        }
    }
}