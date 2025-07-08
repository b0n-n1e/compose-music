package com.loki.utils.datastroe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserLoginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogin(userLogin: UserLoginEntity)

    @Query("SELECT * FROM user_login ORDER BY loginTime DESC LIMIT 1")
    suspend fun getLatestLogin(): UserLoginEntity?

    @Query("DELETE FROM user_login")
    suspend fun clearAll()
} 