package com.findphone.claptofindmyphone.clap.phonefinder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class DefaultPreferences {

    private Context mContext;
    private SharedPreferences SP;
    public  int gif;

    public DefaultPreferences(Context context) {
        this.mContext = context;
    }

    public String read(String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString(key, defValue);
    }

    public void save(String key, String value) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        edit.putString(key, value);
        edit.commit();
    }


}
