package fr.smarquis.applinks

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import fr.smarquis.applinks.Flags.extract
import fr.smarquis.applinks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        invalidateOptionsMenu()
        if (Referrer.isAvailable(this) && !Referrer.hasBeenDisplayed(this)) {
            ReferrerActivity.start(this)
        }
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
        toggleVisibility(binding.intentValue)
        TransitionManager.beginDelayedTransition(binding.scrollView)
    }

    private fun toggleDataValue() {
        toggleVisibility(binding.dataValue)
        TransitionManager.beginDelayedTransition(binding.scrollView)
    }

    private fun toggleExtrasValue() {
        toggleVisibility(binding.extrasValue)
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
            .appendKeyValue("query", object : HashMap<String?, String?>() {
                init {
                    if (data.isHierarchical) {
                        val parameterNames = data.queryParameterNames
                        for (key in parameterNames) {
                            put(key, data.getQueryParameter(key))
                        }
                    }
                }
            })
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
            .addOnSuccessListener(this, OnSuccessListener { pendingDynamicLinkData ->
                if (pendingDynamicLinkData == null) {
                    return@OnSuccessListener
                }
                val message: CharSequence = Html.fromHtml(String.format(getString(R.string.firebase_dynamic_link_received), pendingDynamicLinkData.link))
                Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                    .addCallback(object : Snackbar.Callback() {
                        override fun onShown(sb: Snackbar) {
                            super.onShown(sb)
                            TransitionManager.beginDelayedTransition(binding.scrollView)
                            binding.snackbarSpacer.layoutParams.height = sb.view.height
                            binding.snackbarSpacer.visibility = View.VISIBLE
                        }

                        override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            TransitionManager.beginDelayedTransition(binding.scrollView)
                            binding.snackbarSpacer.visibility = View.GONE
                        }
                    })
                    .show()
                val bundle = Bundle()

                intent.extras?.let(bundle::putAll)
                pendingDynamicLinkData.extensions?.let(bundle::putAll)
                pendingDynamicLinkData.utmParameters.let(bundle::putAll)
                TransitionManager.beginDelayedTransition(binding.scrollView)
                fillExtras(bundle)
            })
    }

    companion object {
        private fun toggleVisibility(view: View) {
            view.visibility = if (view.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
}