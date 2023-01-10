package fr.smarquis.applinks

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.installreferrer.api.ReferrerDetails

internal object Referrer {

    private const val PREFS_NAME = "referrer"
    private const val KEY_FIRST_LAUNCH = "FIRST_LAUNCH"
    private const val KEY_INSTALL_REFERRER = "install_referrer"
    private const val KEY_REFERRER_CLICK_TIMESTAMP = "referrer_click_timestamp_seconds"
    private const val KEY_INSTALL_BEGIN_TIMESTAMP = "install_begin_timestamp_seconds"
    private const val KEY_GOOGLE_PLAY_INSTANT = "google_play_instant"
    private const val KEY_REFERRER_CLICK_TIMESTAMP_SERVER = "referrer_click_timestamp_server_seconds"
    private const val KEY_INSTALL_BEGIN_TIMESTAMP_SERVER = "install_begin_timestamp_server_seconds"
    private const val KEY_INSTALL_VERSION = "install_version"

    data class LocalReferrerDetails(
        val firstLaunch: Long,
        val installReferrer: String?,
        val referrerClickTimestampSeconds: Long,
        val installBeginTimestampSeconds: Long,
        val googlePlayInstantParam: Boolean,
        val referrerClickTimestampServerSeconds: Long,
        val installBeginTimestampServerSeconds: Long,
        val installVersion: String?,
    )


    private val INTENT = Intent("custom-action-local-broadcast")
    val INTENT_FILTER = IntentFilter(INTENT.action)

    private fun prefs(context: Context): SharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun init(context: Context, details: ReferrerDetails) {
        val prefs = prefs(context)
        if (prefs.all.isNotEmpty()) return
        prefs.edit().apply {
            putLong(KEY_FIRST_LAUNCH, System.currentTimeMillis())
            putString(KEY_INSTALL_REFERRER, details.installReferrer)
            putLong(KEY_REFERRER_CLICK_TIMESTAMP, details.referrerClickTimestampSeconds)
            putLong(KEY_INSTALL_BEGIN_TIMESTAMP, details.installBeginTimestampSeconds)
            putBoolean(KEY_GOOGLE_PLAY_INSTANT, details.googlePlayInstantParam)
            putLong(KEY_REFERRER_CLICK_TIMESTAMP_SERVER, details.referrerClickTimestampServerSeconds)
            putLong(KEY_INSTALL_BEGIN_TIMESTAMP_SERVER, details.installBeginTimestampServerSeconds)
            putString(KEY_INSTALL_VERSION, details.installVersion)
        }.apply()
        LocalBroadcastManager.getInstance(context).sendBroadcast(INTENT)
    }

    fun isAvailable(context: Context): Boolean = prefs(context).all.isNotEmpty()

    fun get(context: Context) = prefs(context).run {
        LocalReferrerDetails(
            firstLaunch = getLong(KEY_FIRST_LAUNCH, 0),
            installReferrer = getString(KEY_INSTALL_REFERRER, null),
            referrerClickTimestampSeconds = getLong(KEY_REFERRER_CLICK_TIMESTAMP, 0),
            installBeginTimestampSeconds = getLong(KEY_INSTALL_BEGIN_TIMESTAMP, 0),
            googlePlayInstantParam = getBoolean(KEY_GOOGLE_PLAY_INSTANT, false),
            referrerClickTimestampServerSeconds = getLong(KEY_REFERRER_CLICK_TIMESTAMP_SERVER, 0),
            installBeginTimestampServerSeconds = getLong(KEY_INSTALL_BEGIN_TIMESTAMP_SERVER, 0),
            installVersion = getString(KEY_INSTALL_VERSION, null),
        )
    }

}