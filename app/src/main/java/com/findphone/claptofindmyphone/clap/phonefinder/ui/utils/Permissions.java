package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Permissions extends AppCompatActivity {
    public static final int MIC_PERMISSION_REQUEST_CODE = 777;
    public static final String micPermission = "android.permission.RECORD_AUDIO";
    public static final String[] _micPermisson = {micPermission};

    public static boolean haveMicPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, micPermission) == 0;
    }

    public static void requestMicPermission(AppCompatActivity appCompatActivity, int i) {
        ActivityCompat.requestPermissions(appCompatActivity, _micPermisson, i);
    }
}
