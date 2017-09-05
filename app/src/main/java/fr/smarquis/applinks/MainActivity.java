package fr.smarquis.applinks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.HashMap;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.intentValue)
    TextView intentValue;

    @BindView(R.id.dataValue)
    TextView dataValue;

    @BindView(R.id.extrasValue)
    TextView extrasValue;

    @BindView(R.id.snackbarSpacer)
    Space snackbarSpacer;

    @BindString(R.string.firebase_dynamic_link_received)
    String firebaseLinkReceivedMessage;

    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            supportInvalidateOptionsMenu();
            ReferrerActivity.start(MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fillIntent(getIntent());
        fillData(getData());
        fillExtras(getExtras());

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData == null) {
                            return;
                        }

                        CharSequence message = Html.fromHtml(String.format(firebaseLinkReceivedMessage, pendingDynamicLinkData.getLink()));
                        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                                .addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onShown(Snackbar sb) {
                                        super.onShown(sb);
                                        TransitionManager.beginDelayedTransition(scrollView);
                                        snackbarSpacer.getLayoutParams().height = sb.getView().getHeight();
                                        snackbarSpacer.setVisibility(VISIBLE);
                                    }

                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        TransitionManager.beginDelayedTransition(scrollView);
                                        snackbarSpacer.setVisibility(GONE);
                                    }
                                })
                                .show();

                        Bundle bundle = new Bundle();
                        Bundle initialBundle = getExtras();
                        if (initialBundle != null) {
                            bundle.putAll(initialBundle);
                        }
                        // NOTE: Try to merge internal FDL extras Bundle and display it
                        Bundle firebaseBundle = pendingDynamicLinkData.zzbya();
                        if (firebaseBundle != null) {
                            bundle.putAll(firebaseBundle);
                        }
                        TransitionManager.beginDelayedTransition(scrollView);
                        fillExtras(bundle);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateReceiver, new IntentFilter(ReferrerReceiver.ACTION_UPDATE_DATA));
        supportInvalidateOptionsMenu();
        if (Referrer.isAvailable(this) && !Referrer.hasBeenDisplayed(this)) {
            ReferrerActivity.start(this);
        }
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateReceiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.referrer).setVisible(Referrer.isAvailable(this));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.referrer) {
            ReferrerActivity.start(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    private Bundle getExtras() {
        Intent intent = getIntent();
        return intent != null ? intent.getExtras() : null;
    }

    @Nullable
    private Uri getData() {
        Intent intent = getIntent();
        return intent != null ? intent.getData() : null;
    }

    @OnClick(R.id.intentHeader)
    public void toggleIntentValue() {
        toggleVisibility(intentValue);
        TransitionManager.beginDelayedTransition(scrollView);
    }

    @OnClick(R.id.dataHeader)
    public void toggleDataValue() {
        toggleVisibility(dataValue);
        TransitionManager.beginDelayedTransition(scrollView);
    }

    @OnClick(R.id.extrasHeader)
    public void toggleExtrasValue() {
        toggleVisibility(extrasValue);
        TransitionManager.beginDelayedTransition(scrollView);
    }

    private static void toggleVisibility(View view) {
        view.setVisibility(view.getVisibility() == VISIBLE ? GONE : VISIBLE);
    }

    private void fillIntent(@Nullable Intent intent) {
        if (intent == null) {
            intentValue.setText(Printer.EMPTY);
            return;
        }

        Printer printer = new Printer(this)
                .appendKeyValue("action", intent.getAction())
                .appendKeyValue("categories", intent.getCategories())
                .appendKeyValue("type", intent.getType())
                .appendKeyValue("flags", Flags.extract(intent))
                .appendKeyValue("package", intent.getPackage())
                .appendKeyValue("component", intent.getComponent())
                .appendKeyValue("referrer", Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 ? getReferrer() : null);
        intentValue.setText(printer.stripNewLines().build());
    }

    private void fillData(@Nullable final Uri data) {
        if (data == null) {
            dataValue.setText(Printer.EMPTY);
            return;
        }

        Printer printer = new Printer(this)
                .appendKeyValue("raw", data)
                .appendKeyValue("scheme", data.getScheme())
                .appendKeyValue("host", data.getHost())
                .appendKeyValue("path", data.getPath())
                .appendKeyValue("query", new HashMap<String, String>() {{
                    if (data.isHierarchical()) {
                        Set<String> parameterNames = data.getQueryParameterNames();
                        for (String key : parameterNames) {
                            put(key, data.getQueryParameter(key));
                        }
                    }
                }})
                .appendKeyValue("fragment", data.getFragment());
        dataValue.setText(printer.stripNewLines().build());
    }


    private void fillExtras(@Nullable Bundle extras) {
        if (extras == null) {
            extrasValue.setText(Printer.EMPTY);
            return;
        }
        Printer printer = new Printer(this).appendBundle(extras);
        extrasValue.setText(printer.stripNewLines().build());
    }
}
