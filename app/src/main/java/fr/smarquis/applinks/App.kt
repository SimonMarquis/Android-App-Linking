package fr.smarquis.applinks

import android.app.Application
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener

class App : Application(), InstallReferrerStateListener {

    private lateinit var client: InstallReferrerClient

    override fun onCreate() {
        super.onCreate()
        client = InstallReferrerClient.newBuilder(this).build()
        client.startConnection(this)
    }

    override fun onInstallReferrerSetupFinished(responseCode: Int) {
        Log.d("InstallReferrerResponse", "responseCode=$responseCode")
        if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
            client.installReferrer?.let { Referrer.init(this, it) }
            client.endConnection()
        }
    }

    override fun onInstallReferrerServiceDisconnected() = Unit
}