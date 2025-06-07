package com.loki.utils.network.bean.search

/**
 * @param keyword 关键词
 * @param limit 结果的数量
 * @param offset 偏移量
 */
data class SearchBody(
    val keyword : String? = null,
    val limit : Int = 30,
    val offset : Int = 0
)
