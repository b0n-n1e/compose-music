package com.loki.utils.datastroe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_login")
data class UserLoginEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cookie: String,
    val loginTime: Long = System.currentTimeMillis(),
    val userId: Long? = null,
    val nickname: String? = null,
    val avatarUrl: String? = null
    // 可扩展更多字段，如userId、nickname、avatar等
) 