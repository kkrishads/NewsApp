package com.dreamworks.newsapp.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class TextUtils {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    public static boolean isNullOrEmpty(CharSequence value) {
        return value == null || value.toString().equals("");
    }

    public static String arrayToString(ArrayList<?> array, String delimiter) {
        StringBuilder builder = new StringBuilder();
        if (array.size() > 0) {
            builder.append(array.get(0));
            for (int i = 1; i < array.size(); i++) {
                builder.append(delimiter);
                builder.append(array.get(i));
            }
        }
        return builder.toString();
    }

    public static ArrayList<String> stringToArray(String string) {
        return new ArrayList<>(Arrays.asList(string.split(",")));
    }
}
