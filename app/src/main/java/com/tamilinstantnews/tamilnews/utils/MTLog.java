package com.tamilinstantnews.tamilnews.utils;

import android.util.Log;

import com.tamilinstantnews.tamilnews.BuildConfig;


public class MTLog {

    public static void debug(String str) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (str.length() > 4000) {
            Log.d("Tamil", str.substring(0, 4000));
            debug(str.substring(4000));
        } else {
            Log.d("Tamil", str);
        }
    }

    public static void error(String str) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (str.length() > 4000) {
            Log.e("Tamil", str.substring(0, 4000));
            error(str.substring(4000));
        } else {
            Log.e("Tamil", str);
        }
    }
}
