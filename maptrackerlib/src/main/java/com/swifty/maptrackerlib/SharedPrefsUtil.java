package com.swifty.maptrackerlib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Swifty on 8 Apr, 2016.
 */
public class SharedPrefsUtil {

    private static SharedPrefsUtil sharedPrefsUtil;
    private static SharedPreferences appSharedPrefs;

    private SharedPrefsUtil(Context application) {
        appSharedPrefs = application.getSharedPreferences("PREF", Activity.MODE_PRIVATE);
    }

    public static SharedPrefsUtil getInstance() {
        if (appSharedPrefs == null) throw new RuntimeException("need init sharedPrefsUtil before use");
        return sharedPrefsUtil;
    }

    public static void init(Context context) {
        sharedPrefsUtil = new SharedPrefsUtil(context.getApplicationContext());
    }

    public boolean contains(String key) {
        return appSharedPrefs.contains(key);
    }

    public boolean getBool(String key) {
        return getBool(key, false);
    }

    public boolean getBool(String key, boolean defValue) {
        return appSharedPrefs.getBoolean(key, defValue);
    }

    /**
     * Returns an {@link SharedPreferences.Editor} attached to this pref
     *
     * @return preference editor
     */
    public SharedPreferences.Editor getEditor() {
        return appSharedPrefs.edit();
    }

    public float getFloat(String key, float def) {
        return appSharedPrefs.getFloat(key, def);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return appSharedPrefs.getInt(key, def);
    }

    public long getLong(String key) {
        return appSharedPrefs.getLong(key, 0);
    }

    public SharedPreferences getSharedPreference() {
        return appSharedPrefs;
    }

    public String getString(String key) {
        return appSharedPrefs.getString(key, "");
    }

    public String getString(String key, String def) {
        return appSharedPrefs.getString(key, def);
    }

    public void putBool(String key, boolean v) {
        appSharedPrefs.edit().putBoolean(key, v).apply();
    }

    public void putFloat(String key, float v) {
        appSharedPrefs.edit().putFloat(key, v).apply();
    }

    public void putInt(String key, int v) {
        appSharedPrefs.edit().putInt(key, v).apply();
    }

    public void putLong(String key, long v) {
        appSharedPrefs.edit().putLong(key, v).apply();
    }

    public void putString(String key, String v) {
        appSharedPrefs.edit().putString(key, v).apply();
    }
}