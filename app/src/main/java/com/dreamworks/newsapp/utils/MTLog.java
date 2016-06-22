package com.dreamworks.newsapp.utils;

import android.util.Log;

import com.dreamworks.newsapp.BuildConfig;


public class MTLog {

    public static void debug(String str) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (str.length() > 4000) {
            Log.d("MarxistTamil", str.substring(0, 4000));
            debug(str.substring(4000));
        } else {
            Log.d("MarxistTamil", str);
        }
    }

    public static void error(String str) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (str.length() > 4000) {
            Log.e("MarxistTamil", str.substring(0, 4000));
            error(str.substring(4000));
        } else {
            Log.e("MarxistTamil", str);
        }
    }
}
