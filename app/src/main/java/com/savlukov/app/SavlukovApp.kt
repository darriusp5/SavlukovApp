package com.savlukov.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SavlukovApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
