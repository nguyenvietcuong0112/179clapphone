package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.findphone.claptofindmyphone.clap.phonefinder.R;


public class SharePreferenceUtils {

    private static final String FIND_PHONE_NAME = "FindPhone";
    private static final String KEY_TUTORIAL = "isTutorial";
    private static final String KEY_SOUND = "KEY_SOUND";


    private static volatile SharePreferenceUtils instance;

    private SharedPreferences sharePreference;

    private SharePreferenceUtils(Context context) {
        sharePreference = context.getSharedPreferences(FIND_PHONE_NAME, Context.MODE_PRIVATE);
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SharePreferenceUtils.class) {
                if (instance == null) {
                    instance = new SharePreferenceUtils(context);
                }
            }
        }
        return instance;
    }

    public void setTutorial(boolean value) {
        putBooleanValue(KEY_TUTORIAL, value);
    }

    public boolean isTutorial() {
        return getBooleanValue(KEY_TUTORIAL);
    }

    public void setSetSound(int value) {
        putIntValue(KEY_SOUND, value);
    }

    public int getSound() {
        return getIntValue(KEY_SOUND);
    }

    private void putBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean getBooleanValue(String key) {
        return sharePreference.getBoolean(key, false);
    }

    private void putIntValue(String key, int value) {
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int getIntValue(String key) {
        return sharePreference.getInt(key, R.drawable.cats);
    }

    public static boolean isOrganic(Context context) {
        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pref.getBoolean("organic", true);
    }

    public static void setOrganicValue(Context context, boolean value) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("organic", value);
        editor.apply();
    }

    public static boolean isFirstHome(Context context) {
        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pref.getBoolean("firstHome", true);
    }

    public static void setFirstHome(Context context, boolean value) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("firstHome", value);
        editor.apply();
    }
}
