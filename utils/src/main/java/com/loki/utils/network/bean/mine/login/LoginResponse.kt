package com.loki.utils.network.bean.mine.login

data class LoginResponse(
    val loginType : Int,
    val code : Int,
    val message : String,
    val token : String,
    val profile : LoginProfile,
    val cookie : String
)

data class LoginProfile(
    val nickname : String,
    val avatarUrl : String,
)