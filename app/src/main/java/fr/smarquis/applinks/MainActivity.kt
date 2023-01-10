package fr.smarquis.applinks

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.transition.TransitionManager
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import fr.smarquis.applinks.Flags.extract
import fr.smarquis.applinks.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private val referrerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) = invalidateOptionsMenu()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fillIntent(intent)
        fillData(intent.data)
        fillExtras(intent.extras)
        initFirebaseDeeplink()
        binding.intentHeader.setOnClickListener { toggleIntentValue() }
        binding.dataHeader.setOnClickListener { toggleDataValue() }
        binding.extrasHeader.setOnClickListener { toggleExtrasValue() }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(referrerReceiver, Referrer.INTENT_FILTER)
        invalidateOptionsMenu()
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(referrerReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.referrer).isVisible = Referrer.isAvailable(this)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.referrer) {
            ReferrerActivity.start(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleIntentValue() {
        binding.intentValue.toggleVisibility()
        TransitionManager.beginDelayedTransition(binding.scrollView)
    }

    private fun toggleDataValue() {
        binding.dataValue.toggleVisibility()
        TransitionManager.beginDelayedTransition(binding.scrollView)
    }

    private fun toggleExtrasValue() {
        binding.extrasValue.toggleVisibility()
        TransitionManager.beginDelayedTransition(binding.scrollView)
    }

    private fun fillIntent(intent: Intent?) {
        if (intent == null) {
            binding.intentValue.text = Printer.EMPTY
            return
        }
        val printer = Printer(this)
            .appendKeyValue("action", intent.action)
            .appendKeyValue("categories", intent.categories)
            .appendKeyValue("type", intent.type)
            .appendKeyValue("flags", extract(intent))
            .appendKeyValue("package", intent.getPackage())
            .appendKeyValue("component", intent.component)
            .appendKeyValue("referrer", if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) referrer else null)
        binding.intentValue.text = printer.stripNewLines().build()
    }

    private fun fillData(data: Uri?) {
        if (data == null) {
            binding.dataValue.text = Printer.EMPTY
            return
        }
        val printer = Printer(this)
            .appendKeyValue("raw", data)
            .appendKeyValue("scheme", data.scheme)
            .appendKeyValue("host", data.host)
            .appendKeyValue("path", data.path)
            .appendKeyValue(
                "query",
                if (!data.isHierarchical) emptyMap()
                else data.queryParameterNames.associateWith(data::getQueryParameter)
            )
            .appendKeyValue("fragment", data.fragment)
        binding.dataValue.text = printer.stripNewLines().build()
    }

    private fun fillExtras(extras: Bundle?) {
        if (extras == null) {
            binding.extrasValue.text = Printer.EMPTY
            return
        }
        val printer = Printer(this).appendBundle(extras)
        binding.extrasValue.text = printer.stripNewLines().build()
    }

    private fun initFirebaseDeeplink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                pendingDynamicLinkData ?: return@addOnSuccessListener
                val message = HtmlCompat.fromHtml(String.format(getString(R.string.firebase_dynamic_link_received), pendingDynamicLinkData.link), HtmlCompat.FROM_HTML_MODE_LEGACY)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val bundle = Bundle()
                intent.extras?.let(bundle::putAll)
                pendingDynamicLinkData.extensions?.let(bundle::putAll)
                pendingDynamicLinkData.utmParameters.let(bundle::putAll)
                TransitionManager.beginDelayedTransition(binding.scrollView)
                fillExtras(bundle)
            }
    }

    private fun View.toggleVisibility() {
        visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }
}