package com.nosae.coleader.viewmodels

import androidx.lifecycle.*
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.Friend
import com.nosae.coleader.repository.PunchRepo
import com.nosae.coleader.repository.TeamRepo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Create by NOSAE on 2020/6/7
 */
class CreatePunchViewModel(
    private val repo: PunchRepo,
    private val teamRepo: TeamRepo
): BaseViewModel() {

    private val _createRes = SingleLiveEvent<String>()
    val createRes: LiveData<String?> = _createRes

    val memberResMutable = MutableLiveData<ArrayList<Friend>>(arrayListOf())
    val memberRes = SingleLiveEvent<List<Friend>>()

    var teamId = -1L
    val startAt = MutableLiveData<String>()
    val endAt = MutableLiveData<String>()
    val intro = MutableLiveData<String>()

    fun submit() = launchNetwork {
        val participants = memberResMutable.value!!.map {
            it.id
        }
        val res = repo.createPunch(
            teamId,
            startAt.value!!,
            endAt.value!!,
            intro.value!!,
            participants
        )
        if (res != null && res.errno == "0") {
            _createRes.postCall()
        } else {
            _createRes.postValue("创建失败")
        }
    }

    fun getMember() = launchNetwork {
        val res = teamRepo.getTeamMembers(teamId)
        if (res != null && res.errno == "0") {
            memberRes.postValue(ArrayList(res.data.rows))
        } else {
            memberRes.postCall()
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
            return CreatePunchViewModel(PunchRepo(), TeamRepo()) as T
        }
    }
}