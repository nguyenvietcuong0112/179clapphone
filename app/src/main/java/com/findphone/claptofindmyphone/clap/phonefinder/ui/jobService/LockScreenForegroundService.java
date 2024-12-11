package com.findphone.claptofindmyphone.clap.phonefinder.ui.jobService;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.findphone.claptofindmyphone.clap.phonefinder.Application;
import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice.HomeActivityVoice;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.receiver.ScreenOnOffReceiverListener;


public class LockScreenForegroundService extends Service {
    public static int PENDING_INTENT_REQUEST_CODE = 1002;
    public static final int START_FOREGROUND_ID = 543;
    private static final String TAG = "LSForegroundService";
    private boolean isBroadcastAlreadyRegistered = false;
    ScreenOnOffReceiverListener screenOnOffReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(START_FOREGROUND_ID, new NotificationCompat.Builder(getApplicationContext(), Application.CHANNEL_ID).setSmallIcon(R.drawable.ic_baseline_mic_24).setContentTitle("Voice Lock is Running..").setContentText("Voice Lock Screen is running.. you can turn it off anytime from here...").setContentIntent(PendingIntent.getActivity(getApplicationContext(), PENDING_INTENT_REQUEST_CODE, new Intent(this, HomeActivityVoice.class), PendingIntent.FLAG_IMMUTABLE)).build());
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        ScreenOnOffReceiverListener screenOnOffReceiverListener = new ScreenOnOffReceiverListener(getApplicationContext());
        this.screenOnOffReceiver = screenOnOffReceiverListener;
        if (!this.isBroadcastAlreadyRegistered) {
            registerReceiver(screenOnOffReceiverListener, intentFilter);
            this.isBroadcastAlreadyRegistered = true;
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(this.screenOnOffReceiver);
            this.isBroadcastAlreadyRegistered = false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, "Security exception occured while unregister screen On off Receiver in LockScreenForegroundService class");
        }
        super.onDestroy();
    }
}
