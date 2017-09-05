package fr.smarquis.applinks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.HashSet;

class Flags {
    @SuppressWarnings("deprecation")
    @SuppressLint("InlinedApi")
    private static final SparseArray<String> FLAGS = new SparseArray<String>() {
        {
            put(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT, "FLAG_ACTIVITY_BROUGHT_TO_FRONT");
            put(Intent.FLAG_ACTIVITY_CLEAR_TASK, "FLAG_ACTIVITY_CLEAR_TASK");
            put(Intent.FLAG_ACTIVITY_CLEAR_TOP, "FLAG_ACTIVITY_CLEAR_TOP");
            put(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, "FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET");
            put(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, "FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS");
            put(Intent.FLAG_ACTIVITY_FORWARD_RESULT, "FLAG_ACTIVITY_FORWARD_RESULT");
            put(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, "FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY");
            put(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT, "FLAG_ACTIVITY_LAUNCH_ADJACENT");
            put(Intent.FLAG_ACTIVITY_MULTIPLE_TASK, "FLAG_ACTIVITY_MULTIPLE_TASK");
            put(Intent.FLAG_ACTIVITY_NEW_DOCUMENT, "FLAG_ACTIVITY_NEW_DOCUMENT");
            put(Intent.FLAG_ACTIVITY_NEW_TASK, "FLAG_ACTIVITY_NEW_TASK");
            put(Intent.FLAG_ACTIVITY_NO_ANIMATION, "FLAG_ACTIVITY_NO_ANIMATION");
            put(Intent.FLAG_ACTIVITY_NO_HISTORY, "FLAG_ACTIVITY_NO_HISTORY");
            put(Intent.FLAG_ACTIVITY_NO_USER_ACTION, "FLAG_ACTIVITY_NO_USER_ACTION");
            put(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP, "FLAG_ACTIVITY_PREVIOUS_IS_TOP");
            put(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, "FLAG_ACTIVITY_REORDER_TO_FRONT");
            put(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, "FLAG_ACTIVITY_RESET_TASK_IF_NEEDED");
            put(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS, "FLAG_ACTIVITY_RETAIN_IN_RECENTS");
            put(Intent.FLAG_ACTIVITY_SINGLE_TOP, "FLAG_ACTIVITY_SINGLE_TOP");
            put(Intent.FLAG_ACTIVITY_TASK_ON_HOME, "FLAG_ACTIVITY_TASK_ON_HOME");
            put(Intent.FLAG_DEBUG_LOG_RESOLUTION, "FLAG_DEBUG_LOG_RESOLUTION");
            put(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES, "FLAG_EXCLUDE_STOPPED_PACKAGES");
            put(Intent.FLAG_FROM_BACKGROUND, "FLAG_FROM_BACKGROUND");
            put(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION, "FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
            put(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION, "FLAG_GRANT_PREFIX_URI_PERMISSION");
            put(Intent.FLAG_GRANT_READ_URI_PERMISSION, "FLAG_GRANT_READ_URI_PERMISSION");
            put(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, "FLAG_GRANT_WRITE_URI_PERMISSION");
            put(Intent.FLAG_INCLUDE_STOPPED_PACKAGES, "FLAG_INCLUDE_STOPPED_PACKAGES");
            put(Intent.FLAG_RECEIVER_FOREGROUND, "FLAG_RECEIVER_FOREGROUND");
            put(Intent.FLAG_RECEIVER_NO_ABORT, "FLAG_RECEIVER_NO_ABORT");
            put(Intent.FLAG_RECEIVER_REGISTERED_ONLY, "FLAG_RECEIVER_REGISTERED_ONLY");
            put(Intent.FLAG_RECEIVER_REPLACE_PENDING, "FLAG_RECEIVER_REPLACE_PENDING");
            put(Intent.FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS, "FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS");
        }
    };

    static HashSet<String> extract(@NonNull Intent intent) {
        final int flags = intent.getFlags();
        HashSet<String> flagsStrings = new HashSet<>();
        for (int i = 0; i < FLAGS.size(); i++) {
            int key = FLAGS.keyAt(i);
            String value = FLAGS.valueAt(i);
            if ((flags & key) != 0) {
                flagsStrings.add(value);
            }
        }
        return flagsStrings;
    }

}
