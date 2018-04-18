package com.nikola.jakshic.dagger.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtil {

    private NetworkUtil() {
    }

    public static boolean isActive(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context
                .getApplicationContext() // needed for not leaking activity's context (Detected by LeakCanary)
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}