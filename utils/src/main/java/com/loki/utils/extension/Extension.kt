package com.loki.utils.extension

import android.content.Context
import android.widget.Toast

/**
 * 字符串扩展函数
 */
fun String.limitLength(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength) + "..."
    } else {
        this
    }
}

/**
 * Toast扩展函数
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

val <T : Any> T.TAG: String
    get() = this::class.java.simpleName
