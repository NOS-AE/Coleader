package com.nosae.coleader.data

/**
 * Create by NOSAE on 2020/5/4
 */

object TempData {
    var captchaTime = 0
    var loginTime = 0L
    var token = ""
    var userInfo: UserInfoResDto? = null
    var teamList: List<Team>? = null
    var friendList: List<Friend>? = null
    var canSetAlias = false
}