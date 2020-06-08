package com.nosae.coleader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Date
import com.nosae.coleader.repository.DateRepo
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by NOSAE on 2020/5/31
 */
class DateViewModel(
    private val repo: DateRepo
): BaseViewModel() {

    private val _dateRes = SingleLiveEvent<List<Date>>()
    val dateRes: MutableLiveData<List<Date>?> = _dateRes

    private val format = SimpleDateFormat("yyyyMMdd", Locale.CHINA)

    fun getDate() {
        launchNetwork {
            val calendar = Calendar.getInstance()
            val date = format.format(calendar.time)
            val res = repo.getTimeDate(date)

            if (res != null && res.errno == "0") {
                _dateRes.postValue(res.data.rows)
            } else {
                _dateRes.postCall()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DateViewModel(DateRepo()) as T
        }
    }
}