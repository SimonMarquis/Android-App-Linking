package fr.smarquis.applinks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.text.method.LinkMovementMethod
import fr.smarquis.applinks.databinding.ActivityReferrerBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReferrerActivity : Activity() {

    private lateinit var binding: ActivityReferrerBinding
    private val dateFormatter = DateFormat.getDateInstance()
    private val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Referrer.isAvailable(this)) {
            finish()
            return
        }
        binding = ActivityReferrerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateReferrer()
    }

    private fun updateReferrer() {
        val referrer = Referrer.get(this)
        val installerPackageName =
            if (SDK_INT >= Build.VERSION_CODES.R) packageManager.getInstallSourceInfo(packageName).installingPackageName
            else packageManager.getInstallerPackageName(packageName)
        val printer = Printer(this)
            .appendKeyValue(getString(R.string.referrer_installer_package), installerPackageName)
            .appendKeyValue(getString(R.string.referrer_first_launch), referrer.firstLaunch.asDateTime())
            .appendKeyValue(getString(R.string.referrer_url), referrer.installReferrer)
            .appendKeyValue(getString(R.string.referrer_click_timestamp), referrer.referrerClickTimestampSeconds.times(1_000).asDateTime())
            .appendKeyValue(getString(R.string.referrer_click_timestamp_server), referrer.referrerClickTimestampServerSeconds.times(1_000).asDateTime())
            .appendKeyValue(getString(R.string.referrer_install_timestamp), referrer.installBeginTimestampSeconds.times(1_000).asDateTime())
            .appendKeyValue(getString(R.string.referrer_install_timestamp_server), referrer.installBeginTimestampServerSeconds.times(1_000).asDateTime())
            .appendKeyValue(getString(R.string.referrer_google_play_instant), referrer.googlePlayInstantParam)
            .appendKeyValue(getString(R.string.referrer_version), referrer.installVersion)
        binding.referrerContent.text = printer.stripNewLines().build()
        binding.referrerContent.movementMethod = LinkMovementMethod()
    }

    private fun Long.asDateTime(): String? {
        if (this == 0L) return null
        val date = Date(this)
        return dateFormatter.format(date) + " - " + sdf.format(date)
    }

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, ReferrerActivity::class.java))
    }
}