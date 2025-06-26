package com.loki.utils.network.service

import com.loki.utils.network.ApiConstant
import com.loki.utils.network.bean.home.BannerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeService {

    @GET(ApiConstant.Banner.API_BANNER)
    suspend fun getBanners(
        @Query("type") type: Int = 1 // 0: pc, 1: android, 2: iphone, 3: ipad
    ): BannerResponse
} 