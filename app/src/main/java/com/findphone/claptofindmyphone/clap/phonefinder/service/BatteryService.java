package com.findphone.claptofindmyphone.clap.phonefinder.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;


import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.IBinder;
import android.os.SystemClock;

import android.util.Log;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.findphone.claptofindmyphone.clap.phonefinder.dialog.ChargeDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import io.paperdb.Book;
import io.paperdb.Paper;


public class BatteryService extends Service {

    boolean plugin = false;
    boolean plugout = false;
    boolean first = true;
    boolean onstart = false;
    boolean ischarging = true;
    boolean isnotcharging = true;
    private final BroadcastReceiver batteryChangeReceiver = new BroadcastReceiver() {
        @SuppressLint("WrongConstant")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!BatteryService.this.first) {
                int intExtra = intent.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
                int intExtra2 = intent.getIntExtra("plugged", -1);
                boolean z = intExtra == 2 || intExtra == 5;
                if (!(intExtra2 == 2 || intExtra2 == 1) || !z) {
                    if (!BatteryService.this.isnotcharging) {
                        Paper.book().write("end_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                        BatteryService.this.updateBatteryData(intent, false);
                        LocalBroadcastManager.getInstance(BatteryService.this).sendBroadcast(new Intent("com.durga.action.close"));
                        Intent intent2 = new Intent(BatteryService.this, ChargeDialog.class);
                        intent2.putExtra(NotificationCompat.CATEGORY_STATUS, "disconnected");
                        Paper.book().write("for_boost", false);
                        intent2.setFlags(402653184);
                        BatteryService.this.startActivity(intent2);
                    }
                    BatteryService.this.isnotcharging = true;
                    BatteryService.this.ischarging = false;
                } else {
                    if (!BatteryService.this.ischarging) {
                        Paper.book().write("start_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                        BatteryService.this.updateBatteryData(intent, true);
                        LocalBroadcastManager.getInstance(BatteryService.this).sendBroadcast(new Intent("com.durga.action.close"));
                        Intent intent3 = new Intent(BatteryService.this, ChargeDialog.class);
                        intent3.putExtra(NotificationCompat.CATEGORY_STATUS, "connected");
                        Paper.book().write("for_boost", false);
                        intent3.setFlags(402653184);
                        BatteryService.this.startActivity(intent3);
                    }
                    BatteryService.this.ischarging = true;
                    BatteryService.this.isnotcharging = false;
                }
            }
            BatteryService.this.first = false;
            if (!BatteryService.this.onstart) {
                int intExtra3 = intent.getIntExtra("plugged", -1);
                if (intExtra3 == 1 || intExtra3 == 2) {
                    Log.e("Charger", "start");
                    if (!BatteryService.this.plugin) {
                        BatteryService.this.plugin = true;
                        BatteryService.this.plugout = false;
                        return;
                    }
                    return;
                }
                if (!BatteryService.this.plugout) {
                    BatteryService.this.plugout = true;
                    BatteryService.this.plugin = false;
                }
                Log.e("Charger", "remove");
                return;
            }
            BatteryService.this.onstart = false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);

    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {

        this.onstart = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.batteryChangeReceiver, intentFilter);
        return START_STICKY;
    }


    public void updateBatteryData(Intent intent, boolean z) {
        String str;
        switch (intent.getIntExtra("health", 0)) {
            case 2:
                str = "Good";
                break;
            case 3:
                str = "OverHeat";
                break;
            case 4:
                str = "Dead";
                break;
            case 5:
                str = "Over Voltage";
                break;
            case 6:
                str = "Unspecified Failure";
                break;
            case 7:
                str = "Cold";
                break;
            default:
                str = "";
                break;
        }
        if (!str.equals("")) {
            Paper.book().write("Health", str);
        }
        int intExtra = intent.getIntExtra("level", -1);
        int intExtra2 = intent.getIntExtra("scale", -1);
        if (!(intExtra == -1 || intExtra2 == -1)) {
            int i = (int) ((((float) intExtra) / ((float) intExtra2)) * 100.0f);
            if (z) {
                Paper.book().write("Battery_pct_start", Integer.valueOf(i));
            }
        }
        int intExtra3 = intent.getIntExtra("temperature", 0);
        if (intExtra3 > 0) {
            Book book = Paper.book();
            book.write("Temperature", (((float) intExtra3) / 10.0f) + "°C");
        }
        int intExtra4 = intent.getIntExtra("voltage", 0);
        if (intExtra4 > 0) {
            Book book2 = Paper.book();
            book2.write("Voltage", (((float) intExtra4) / 1000.0f) + " V");
        }
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        Intent intent2 = new Intent(getApplicationContext(), getClass());
        intent2.setPackage(getPackageName());
        ((AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, PendingIntent.getService(getApplicationContext(), 1, intent2, Intent.FILL_IN_ACTION));
        super.onTaskRemoved(intent);
    }

   /* @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            BroadcastReceiver broadcastReceiver = this.batteryChangeReceiver;
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception unused) {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
