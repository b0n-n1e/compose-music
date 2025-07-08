package com.loki.utils.datastroe

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserLoginEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLoginDao(): UserLoginDao
} 