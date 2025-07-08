package com.loki.compose_music

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        com.loki.utils.datastroe.UserManager.init(this)
    }
}