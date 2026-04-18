package com.team3.sneakx

import android.app.Application

class SneakXApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.seedAdminIfNeeded()
    }
}
