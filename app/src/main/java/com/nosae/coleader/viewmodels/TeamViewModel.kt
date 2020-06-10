package com.nosae.coleader.viewmodels

import androidx.lifecycle.*
import com.nosae.coleader.base.SingleLiveEvent
import com.nosae.coleader.data.JoinTeamUrlResDto
import com.nosae.coleader.data.TeamInfo
import com.nosae.coleader.repository.TeamRepo

/**
 * Create by NOSAE on 2020/5/19
 */
class TeamViewModel(
    private val repo: TeamRepo
): BaseViewModel() {

    var teamId = -1L

    val team = MutableLiveData<TeamInfo>()
    private val _teamRes = SingleLiveEvent<String>()
    val teamRes: LiveData<String?> = _teamRes

    private val _teamUrl = SingleLiveEvent<JoinTeamUrlResDto.Data>()
    val teamUrl: LiveData<JoinTeamUrlResDto.Data?> = _teamUrl

    val isMember = team.map {
        it.isMember
    }

    fun getInfo() {
        launchNetwork {
            val res = repo.getTeamInfo(teamId)
            if (res == null || res.errno != "0") {
                _teamRes.postValue("获取团队信息失败")
            } else {
                team.postValue(res.data)
            }
        }
    }

    fun shareTeam() = launchNetwork {
        val res = repo.getTeamUrl(teamId)
        if (res != null && res.errno == "0") {
            _teamUrl.postValue(res.data)
        } else {
            _teamUrl.postCall()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TeamViewModel(TeamRepo()) as T
        }
    }
}