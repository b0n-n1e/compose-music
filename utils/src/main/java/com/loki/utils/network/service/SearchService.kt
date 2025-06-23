package com.loki.utils.network.service

import com.loki.utils.network.ApiConstant
import com.loki.utils.network.bean.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(ApiConstant.Search.API_SEARCH)
    suspend fun searchSongs(
        @Query("keywords") keyword: String,
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0
    ): SearchResponse
}