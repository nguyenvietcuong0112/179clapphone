package com.findphone.claptofindmyphone.clap.phonefinder.broadcast;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.findphone.claptofindmyphone.clap.phonefinder.model.DefaultPreferences;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.VocalService;


public class MyBroadcastReceiver extends BroadcastReceiver {
    private Intent i;
    private Context mCtx;

    public void onReceive(Context context, Intent intent) {
        this.mCtx = context;
        if (!new DefaultPreferences(context).read("StopService", "1").equals("1")) {
            startAlert(context);
            if (!isMyServiceRunning(VocalService.class)) {
                this.i = new Intent(context, VocalService.class);
                context.startService(this.i);
            }
        }
    }

    public void startAlert(Context context) {
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((long) 1000), PendingIntent.getBroadcast(context, 234, new Intent(context, MyBroadcastReceiver.class), PendingIntent.FLAG_IMMUTABLE));
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        for (RunningServiceInfo runningServiceInfo : ((ActivityManager) this.mCtx.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
