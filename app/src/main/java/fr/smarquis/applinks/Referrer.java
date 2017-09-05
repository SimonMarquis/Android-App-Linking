package fr.smarquis.applinks;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

final class Referrer {

    private static final String PREFS_NAME = "referrer";
    private static final String FIRST_LAUNCH = "FIRST_LAUNCH";
    private static final String REFERRER_DATE = "REFERRER_DATE";
    private static final String REFERRER_DATA = "REFERRER_DATA";
    private static final String DISPLAYED = "DISPLAYED";

    private Referrer() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    static void setFirstLaunch(Context context) {
        SharedPreferences sp = prefs(context);
        if (!sp.contains(FIRST_LAUNCH)) {
            sp.edit().putLong(FIRST_LAUNCH, System.currentTimeMillis()).apply();
        }
    }

    static String getFirstLaunch(Context context) {
        long time = prefs(context).getLong(FIRST_LAUNCH, 0);
        if (time > 0) {
            return formatDateTime(time);
        }
        return null;
    }

    static boolean isAvailable(Context context) {
        return prefs(context).contains(REFERRER_DATE);
    }

    static void setDisplayed(Context context) {
        prefs(context).edit().putBoolean(DISPLAYED, true).apply();
    }

    static boolean hasBeenDisplayed(Context context) {
        return prefs(context).getBoolean(DISPLAYED, false);
    }

    static void setReferrer(Context context, String data) {
        SharedPreferences sp = prefs(context);
        sp.edit().putLong(REFERRER_DATE, System.currentTimeMillis())
                .putString(REFERRER_DATA, data)
                .apply();
    }

    @Nullable
    static String getDate(Context context) {
        long time = prefs(context).getLong(REFERRER_DATE, 0);
        if (time > 0) {
            return formatDateTime(time);
        }
        return null;
    }

    @NonNull
    private static String formatDateTime(long time) {
        Date date = new Date(time);
        return DateFormat.getDateInstance().format(date) + " - " + new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(date);
    }

    @Nullable
    static String getRawData(Context context) {
        return prefs(context).getString(REFERRER_DATA, null);
    }

    @Nullable
    static String getDecodedData(Context context) {
        String raw = getRawData(context);
        if (raw == null) {
            return null;
        }

        String first = urlDecode(raw);
        if (first == null || first.equals(raw)) {
            return null;
        }
        String second = urlDecode(first);
        if (second == null || second.equals(raw)) {
            return first;
        }
        return second;
    }

    @Nullable
    private static String urlDecode(@NonNull String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
