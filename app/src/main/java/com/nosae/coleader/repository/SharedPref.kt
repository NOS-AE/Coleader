package com.nosae.coleader.repository

import com.nosae.coleader.data.UserInfo
import com.nosae.coleader.data.UserInfoResDto
import com.nosae.coleader.data.UserPrefDelegate

/**
 * Create by NOSAE on 2020/4/22
 */
object SharedPref {
    var userEmail by UserPrefDelegate("")
    var userName by UserPrefDelegate("")
    var userPassword by UserPrefDelegate("")
    var loginAccount by UserPrefDelegate("")
    var hasSetAlias by UserPrefDelegate(false)
}