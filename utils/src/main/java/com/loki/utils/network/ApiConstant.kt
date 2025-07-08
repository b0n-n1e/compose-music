package com.loki.utils.network

object ApiConstant {

    const val BASE_URL = "http://10.24.3.151:4000/"

    object Search{
        const val API_SEARCH = "search"
    }

    object Banner{
        const val API_BANNER = "banner"
    }

    object Auth {
        const val API_LOGIN_CELLPHONE = "login/cellphone"
        const val API_CAPTCHA_SENT = "captcha/sent"
        const val API_CAPTCHA_VERIFY = "captcha/verify"
        const val API_LOGIN_REFRESH = "login/refresh"
    }
}