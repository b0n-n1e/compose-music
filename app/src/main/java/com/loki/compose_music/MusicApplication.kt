package com.loki.compose_music

import android.app.Application
import com.loki.utils.datastroe.UserManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserManager.init(this)
    }
}