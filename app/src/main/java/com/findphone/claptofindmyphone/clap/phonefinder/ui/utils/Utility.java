package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.File;

public class Utility {
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
    }

    public static void install_app(Context context, String str) {
        if (isNetConnected(context)) {
            Intent addFlags = new Intent("android.intent.action.VIEW", Uri.parse(str)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (addFlags.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(addFlags);
                return;
            }
            return;
        }
        Toast.makeText(context, "No Internet connection found ! Please ensure Wi-fi or Data connection is on", Toast.LENGTH_LONG).show();
    }


    public static void moreapps(Context context) {
        if (isNetConnected(context)) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=" + context.getPackageName()));
                intent.setPackage("com.android.vending");
                context.startActivity(intent);
            } catch (Exception unused) {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=" + context.getPackageName())));
            }
        }
    }
    public static boolean isEmulator1(Context context) {
        return (Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.contains("generic") ||
                Build.FINGERPRINT.contains("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                "google_sdk".equals(Build.PRODUCT) ||
                !hasTelephony(context) ||
                checkForEmulatorFiles());
    }

    public static boolean checkForEmulatorFiles() {
        String[] knownEmulatorFiles = {
                "/dev/socket/qemud",
                "/dev/qemu_pipe",
                "/system/lib/libc_malloc_debug_qemu.so",
                "/sys/qemu_trace",
                "/system/bin/qemu-props"
        };

        for (String file : knownEmulatorFiles) {
            File f = new File(file);
            if (f.exists()) {
                return true;  // Phát hiện file giả lập
            }
        }
        return false;
    }

    public static boolean isEmulator(Context context) {
        boolean result = isEmulator1(context);  //
        if (!hasTelephony(context)) {
            result = true;  // Nếu không có telephony (SIM), khả năng cao là giả lập
        }
        return result;

    }
    public static boolean hasTelephony(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }
}
