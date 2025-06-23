package com.loki.utils.extension

fun String.limitLength(
    max: Int
): String =
    if (this.length > max)
        this.take(max) + "..."
    else this
