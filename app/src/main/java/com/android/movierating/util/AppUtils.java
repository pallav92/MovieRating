package com.android.movierating.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by pallav on 14/11/17.
 */

public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static <T> T parseJson(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }

    public static <T> T parseJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }

    public static String getJson(Object profile) {
        return new Gson().toJson(profile);
    }
}
