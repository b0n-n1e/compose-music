package com.loki.utils.network.service

import com.loki.utils.network.bean.mine.AccountResponse
import com.loki.utils.network.bean.mine.UserDetailResponse
import com.loki.utils.network.bean.mine.LogoutResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    // 获取账号信息
    @GET("user/account")
    suspend fun getAccount(): AccountResponse

    // 获取用户详情
    @GET("user/detail")
    suspend fun getUserDetail(
        @Query("uid") uid: Long
    ): UserDetailResponse

    // 退出登录
    @GET("logout")
    suspend fun logout(): LogoutResponse
}