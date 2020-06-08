package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Punch
import com.nosae.coleader.repository.PunchRepo

/**
 * Create by NOSAE on 2020/6/7
 */
class MainViewModel(
    private val punchRepo: PunchRepo
): BaseViewModel() {

    private val _punchRes = SingleLiveEvent<List<Punch>>()
    val punchRes: LiveData<List<Punch>?> = _punchRes

    fun getLastPunch() = launchNetwork {
        val res = punchRepo.getUserPunch(1)
        if (res != null && res.errno == "0") {
            _punchRes.postValue(res.data.rows)
        } else {
            _punchRes.postCall()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(PunchRepo()) as T
        }
    }
}