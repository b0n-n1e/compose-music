package com.loki.utils.network.service

import com.loki.utils.network.ApiConstant.BASE_URL
import com.loki.utils.network.bean.search.SearchBody
import com.loki.utils.network.bean.search.SearchResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface SearchService {

    @GET(BASE_URL)
    suspend fun getSearchData(@Body searchBody: SearchBody) : SearchResponse
}