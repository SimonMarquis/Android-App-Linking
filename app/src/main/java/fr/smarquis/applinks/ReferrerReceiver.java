package fr.smarquis.applinks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ReferrerReceiver extends BroadcastReceiver {

    static final String ACTION_UPDATE_DATA = "ACTION_UPDATE_DATA";
    private static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String KEY_REFERRER = "referrer";

    public ReferrerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.e("ReferrerReceiver", "Intent is null");
            return;
        }
        if (!ACTION_INSTALL_REFERRER.equals(intent.getAction())) {
            Log.e("ReferrerReceiver", "Wrong action! Expected: " + ACTION_INSTALL_REFERRER + " but was: " + intent.getAction());
            return;
        }
        Bundle extras = intent.getExtras();
        if (intent.getExtras() == null) {
            Log.e("ReferrerReceiver", "No data in intent");
            return;
        }

        Referrer.setReferrer(context, extras.getString(KEY_REFERRER));
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_UPDATE_DATA));
    }
}