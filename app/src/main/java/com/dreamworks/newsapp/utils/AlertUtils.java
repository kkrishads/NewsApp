package com.dreamworks.newsapp.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class AlertUtils {

    private static void showSnackbar(View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showShortSnackbar(View view, String message) {
        showSnackbar(view, message, Snackbar.LENGTH_SHORT);
    }

    public static void showLongSnackbar(View view, String message) {
        showSnackbar(view, message, Snackbar.LENGTH_LONG);
    }
}
