package fr.smarquis.applinks

import android.content.Context
import android.content.SharedPreferences
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object Referrer {

    private const val PREFS_NAME = "referrer"
    private const val FIRST_LAUNCH = "FIRST_LAUNCH"
    private const val REFERRER_DATE = "REFERRER_DATE"
    private const val REFERRER_DATA = "REFERRER_DATA"
    private const val DISPLAYED = "DISPLAYED"

    private fun prefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setFirstLaunch(context: Context) {
        val sp = prefs(context)
        if (!sp.contains(FIRST_LAUNCH)) {
            sp.edit().putLong(FIRST_LAUNCH, System.currentTimeMillis()).apply()
        }
    }

    @JvmStatic
    fun getFirstLaunch(context: Context): String? {
        val time = prefs(context).getLong(FIRST_LAUNCH, 0)
        return if (time > 0) {
            formatDateTime(time)
        } else null
    }

    @JvmStatic
    fun isAvailable(context: Context): Boolean {
        return prefs(context).contains(REFERRER_DATE)
    }

    @JvmStatic
    fun setDisplayed(context: Context) {
        prefs(context).edit().putBoolean(DISPLAYED, true).apply()
    }

    fun hasBeenDisplayed(context: Context): Boolean {
        return prefs(context).getBoolean(DISPLAYED, false)
    }

    fun setReferrer(context: Context, data: String?) {
        val sp = prefs(context)
        sp.edit().putLong(REFERRER_DATE, System.currentTimeMillis())
            .putString(REFERRER_DATA, data)
            .apply()
    }

    @JvmStatic
    fun getDate(context: Context): String? {
        val time = prefs(context).getLong(REFERRER_DATE, 0)
        return if (time > 0) {
            formatDateTime(time)
        } else null
    }

    private fun formatDateTime(time: Long): String {
        val date = Date(time)
        return DateFormat.getDateInstance().format(date) + " - " + SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(date)
    }

    @JvmStatic
    fun getRawData(context: Context): String? {
        return prefs(context).getString(REFERRER_DATA, null)
    }

    @JvmStatic
    fun getDecodedData(context: Context): String? {
        val raw = getRawData(context) ?: return null
        val first = urlDecode(raw)
        if (first == null || first == raw) {
            return null
        }
        val second = urlDecode(first)
        return if (second == null || second == raw) {
            first
        } else second
    }

    private fun urlDecode(input: String): String? {
        return try {
            URLDecoder.decode(input, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            null
        }
    }
}