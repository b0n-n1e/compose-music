package com.loki.utils.network.service

import com.loki.utils.network.bean.login.QrKeyResponse
import com.loki.utils.network.bean.login.QrCreateResponse
import com.loki.utils.network.bean.login.QrCheckResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    // 1. 获取二维码key
    @GET("login/qr/key")
    suspend fun getQrKey(
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): QrKeyResponse

    // 2. 生成二维码
    @GET("login/qr/create")
    suspend fun createQr(
        @Query("key") key: String,
        @Query("qrimg") qrimg: Int = 1,
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): QrCreateResponse

    // 3. 检查二维码扫码状态
    @GET("login/qr/check")
    suspend fun checkQrStatus(
        @Query("key") key: String,
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): QrCheckResponse
} 