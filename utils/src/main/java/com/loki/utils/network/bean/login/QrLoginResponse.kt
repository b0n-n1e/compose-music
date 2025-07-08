package com.loki.utils.network.bean.login

data class QrKeyResponse(
    val data: QrKeyData?,
    val code: Int?
)
data class QrKeyData(
    val code: Int?,
    val unikey: String?
)

data class QrCreateResponse(
    val code: Int?,
    val data: QrCreateData?
)
data class QrCreateData(
    val qrurl: String?,
    val qrimg: String?
)

data class QrCheckResponse(
    val code: Int?,
    val message: String?,
    val cookie: String?
) 