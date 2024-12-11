package com.findphone.claptofindmyphone.clap.phonefinder.ui.services;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.android.billingclient.BuildConfig;
import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.model.DefaultPreferences;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.alert_vocal.VocalAlert;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.thread.DetectClapClap;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.thread.DetectorThread;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.thread.RecorderThread;


import java.util.Timer;
import java.util.TimerTask;


public class VocalService extends Service implements OnSignalsDetectedListener {

    public static final int DETECT_NONE = 0;
    public static final int DETECT_WHISTLE = 1;
    private static final int NOTIFICATION_Id = 1;

    public static int selectedDetection;
    private DefaultPreferences classesApp;
    private DetectorThread detectorThread;
    private RecorderThread recorderThread;


    private int counter = 0;
    private Timer timer;
    private TimerTask timerTask;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        startTimer();
        startDetection();
        return START_STICKY;
    }

    public void startDetection() {
        try {
            new DetectClapClap(getApplicationContext()).listen();
            this.classesApp = new DefaultPreferences(this);
            this.classesApp.save("detectClap", "0");
        } catch (Exception unused) {
            Toast.makeText(this, "Recorder not supported by this device", Toast.LENGTH_LONG).show();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
        stopSelf();
        RecorderThread recorderThread = this.recorderThread;
        if (recorderThread != null) {
            recorderThread.stopRecording();
            this.recorderThread = null;
        }
        DetectorThread detectorThread = this.detectorThread;
        if (detectorThread != null) {
            detectorThread.stopDetection();
            this.detectorThread = null;
        }
        selectedDetection = 0;
        Toast.makeText(this, "Detection stopped", Toast.LENGTH_LONG).show();
    }

    public void onWhistleDetected() {
        Intent intent = new Intent(this, VocalAlert.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(this, "Clap detected", Toast.LENGTH_LONG).show();
        stopSelf();
    }

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                counter++;
            }
        };
        timer.schedule(timerTask, 3000, 3000);
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(1, new Notification(), FOREGROUND_SERVICE_TYPE_MICROPHONE);
            }
        }

    }

    @SuppressLint("NewApi")
    @RequiresApi(api = 26)
    private void startMyOwnForeground() {
        String str = BuildConfig.APPLICATION_ID;
        NotificationChannel notificationChannel = new NotificationChannel(str, "Secret Camera", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(-16776961);
        notificationChannel.setLockscreenVisibility(0);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        startForeground(2, new NotificationCompat.Builder(this, str).setOngoing(true).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("App is running in background").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).build());


    }
}
