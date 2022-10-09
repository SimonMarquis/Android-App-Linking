package fr.smarquis.applinks

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Referrer.setFirstLaunch(this)
    }
}