package com.findphone.claptofindmyphone.clap.phonefinder.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.findphone.claptofindmyphone.clap.phonefinder.ui.jobService.LockScreenForegroundService;


public class MyBootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null || !intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            Log.e("xcxcxc", "OnBootCompleted : startForegroundService called");
            context.startForegroundService(new Intent(context, LockScreenForegroundService.class));
            return;
        }
        Log.e("xcxcxc", "OnBootCompleted : startService called");
        context.startService(new Intent(context, LockScreenForegroundService.class));
    }
}
