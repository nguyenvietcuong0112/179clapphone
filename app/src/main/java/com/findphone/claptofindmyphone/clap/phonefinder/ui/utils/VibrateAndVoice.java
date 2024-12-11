package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.findphone.claptofindmyphone.clap.phonefinder.R;


public class VibrateAndVoice {
    public static void vibrate(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                VibrationEffect createOneShot;
                if (context.getSharedPreferences("voice_recognition_preference", 0).getBoolean("vibration_flag", true)) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT < 26) {
                        if (vibrator != null) {
                            vibrator.vibrate(50L);
                        }
                    } else if (vibrator == null || (createOneShot = VibrationEffect.createOneShot(50L, -1)) == null) {
                    } else {
                        vibrator.vibrate(createOneShot);
                    }
                }
            }
        }).start();
    }

    public static void invalidPassword(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (context.getSharedPreferences("voice_recognition_preference", 0).getBoolean("sound_flag", true)) {
                    MediaPlayer create = MediaPlayer.create(context, (int) R.raw.invalid_pass);
                    create.start();
                    create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                }
            }
        }).start();
    }

    public static void passwordAccepted(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (context.getSharedPreferences("voice_recognition_preference", 0).getBoolean("sound_flag", true)) {
                    MediaPlayer create = MediaPlayer.create(context, (int) R.raw.pass_accepted);
                    create.start();
                    create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                }
            }
        }).start();
    }
}
