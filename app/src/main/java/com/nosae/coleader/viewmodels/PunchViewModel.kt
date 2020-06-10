package com.nosae.coleader.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Punch
import com.nosae.coleader.repository.PunchRepo

/**
 * Create by NOSAE on 2020/6/6
 */
class PunchViewModel(
    private val repo: PunchRepo
): BaseViewModel() {

    val punch = MutableLiveData<Punch>()

    private val _punchRes = SingleLiveEvent<String>()
    val punchRes: LiveData<String?> = _punchRes

    fun punch() = launchNetwork {
        val p = punch.value!!
        if (p.status == 1) {
            _punchRes.postValue("已经打过卡了")
            return@launchNetwork
        }

        val res = repo.punch(p.teamId, p.id)
        if (res != null && res.errno == "0") {
            _punchRes.postValue("打卡成功")
        } else {
            _punchRes.postValue("打卡失败")
        }
        p.status = 1
        punch.postValue(p)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PunchViewModel(PunchRepo()) as T
        }
    }
}