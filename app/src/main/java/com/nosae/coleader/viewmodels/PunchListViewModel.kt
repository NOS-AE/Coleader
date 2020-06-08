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
class PunchListViewModel(
    private val repo: PunchRepo
): BaseViewModel() {

    private val _listRes = SingleLiveEvent<List<Punch>>()
    val listRes: LiveData<List<Punch>?> = _listRes

    var teamId = -1L
    val isAdmin = MutableLiveData(false)

    fun getPunches() = launchNetwork {
        val res = if (teamId != -1L) {
            repo.getTeamPunch(teamId = teamId)
        } else {
            repo.getUserPunch()
        }
        if (res != null && res.errno == "0") {
            _listRes.postValue(res.data.rows)
        } else {
            _listRes.postCall()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PunchListViewModel(PunchRepo()) as T
        }
    }
}