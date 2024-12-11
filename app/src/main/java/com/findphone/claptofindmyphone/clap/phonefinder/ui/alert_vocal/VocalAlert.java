package com.findphone.claptofindmyphone.clap.phonefinder.ui.alert_vocal;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityAlertvocalBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.model.DefaultPreferences;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.getstart.GetStartActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.VocalService;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.setting.SettingFindPhoneActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SharePreferenceUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.InterCallback;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;


import java.io.IOException;

//import vocsy.ads.GoogleAds;

public class VocalAlert extends BaseActivity implements OnCompletionListener, Callback {
    public static MediaPlayer mySong;
    SurfaceHolder mHolder;
    MediaPlayer mp;
    Parameters params;
    SurfaceView preview;
    private Camera camera;
    private DefaultPreferences defaultPreferences;
    private boolean deviceHasCameraFlash;
    private String flashbox;
    private boolean hasFlash;
    private boolean isFlashOn;
    private Context mCtx;
    private CountDownTimer mTimer = null;
    private boolean run;
    private String soundbox;
    private Vibrator v;
    private String vibratebox;
    ActivityAlertvocalBinding binding;

    public void onPointerCaptureChanged(boolean z) {

    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }


    @Override
    public void bind() {
        SystemUtil.setLocale(this);
        SystemConfiguration.setStatusBarColor(this, R.color.mycolorwhite, SystemConfiguration.IconColor.ICON_DARK);
        binding = ActivityAlertvocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.defaultPreferences = new DefaultPreferences(this);
        setProgresseBarInvisible();
        this.mCtx = this;
        this.deviceHasCameraFlash = getPackageManager().hasSystemFeature("android.hardware.camera.flash");
        initialize();
       Glide.with(VocalAlert.this).asDrawable().load(SharePreferenceUtils.getInstance(this).getSound()).into(binding.stop);
        DefaultPreferences defaultPreferences = new DefaultPreferences(this.mCtx);
        defaultPreferences.save("StopService", "1");
        this.flashbox = defaultPreferences.read("flashbox", "1");
        this.vibratebox = defaultPreferences.read("vibratebox", "1");
        this.soundbox = defaultPreferences.read("soundbox", "1");
        defaultPreferences.save("detectClap", "1");
        stopService(new Intent(getBaseContext(), VocalService.class));
        setvolume(Integer.parseInt(defaultPreferences.read("seekBar", "50")));
        getWindow().addFlags(128);
        if (this.flashbox.equals("1") && this.deviceHasCameraFlash) {
            this.isFlashOn = false;
            getCamera();
            startTimer(1000, true);
        }
        if (this.vibratebox.equals("1")) {
            runvibrate(true);
        }
        if (this.soundbox.equals("1")) {
            runsong();
        }
        setProgresseBarInvisible();
        loadAdsNative();
        loadInter();
    }

    private void setvolume(int i) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(3, (int) (((float) audioManager.getStreamMaxVolume(3)) * (((float) i) / 100.0f)), 0);
    }

    private void runvibrate(boolean z) {
        this.run = z;
        Context context = this.mCtx;
        this.v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        new Thread(() -> {
            while (VocalAlert.this.run) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    VocalAlert.this.v.vibrate(1000);
                } catch (Exception unused) {
                }
            }
        }).start();
    }

    private void runsong() {
        String isoSound = SystemUtil.getIsoSound(this);
        if (isoSound == "bird") {
            mySong = MediaPlayer.create(this, R.raw.music_bird);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "cats") {
            mySong = MediaPlayer.create(this, R.raw.music_cat);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "pig") {
            mySong = MediaPlayer.create(this, R.raw.music_pig);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "cow") {
            mySong = MediaPlayer.create(this, R.raw.music_cow);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "duck") {
            mySong = MediaPlayer.create(this, R.raw.music_duck);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "frog") {
            mySong = MediaPlayer.create(this, R.raw.music_frog);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "goat") {
            mySong = MediaPlayer.create(this, R.raw.music_goat);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "mouse") {
            mySong = MediaPlayer.create(this, R.raw.music_mouse);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "chicken") {
            mySong = MediaPlayer.create(this, R.raw.music_chicken);
            mySong.setOnCompletionListener(this);
            mySong.start();
        } else if (isoSound == "dog") {
            mySong = MediaPlayer.create(this, R.raw.music_dog);
            mySong.setOnCompletionListener(this);
            mySong.start();
        }

    }

    private void initialize() {
        binding.stop.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Admob.getInstance().showInterAds(VocalAlert.this, Constant.interAlert, new InterCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        if (VocalAlert.this.mTimer != null) {
                            VocalAlert.this.mTimer.cancel();
                        }
                        VocalAlert.this.turnOffFlash();
                        if (VocalAlert.mySong != null) {
                            VocalAlert.mySong.release();
                        }
                        VocalAlert.this.run = false;
                        VocalAlert.this.runvibrate(false);
                        Intent intent = new Intent(VocalAlert.this, SettingFindPhoneActivity.class);
                      //  intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
                        VocalAlert.this.startActivity(intent);
                        VocalAlert.this.finish();
                    }
                });

            }
        });
      /*  binding.home.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

            }
        });*/
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public void startTimer(long j, boolean z) {
        this.run = z;
        if (this.run) {
            this.mTimer = new CountDownTimer(j, 1000) {
                public void onTick(long j) {
                }

                public void onFinish() {
                    if (VocalAlert.this.isFlashOn) {
                        VocalAlert.this.turnOffFlash();
                    } else {
                        VocalAlert.this.turnOnFlash();
                    }
                    VocalAlert vocalAlert = VocalAlert.this;
                    vocalAlert.startTimer(1000, vocalAlert.run);
                }
            };
            this.mTimer.start();
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHolder = surfaceHolder;
        try {
            this.camera.setPreviewDisplay(this.mHolder);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            this.camera.stopPreview();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.mHolder = null;
    }


    public void onDestroy() {
        try {
            SettingFindPhoneActivity.activitySetting.finish();
        } catch (Exception unused) {
        }
        super.onDestroy();
    }


    public void onPause() {
        super.onPause();
        turnOffFlash();
    }


    public void onRestart() {
        super.onRestart();
    }


    public void onResume() {
        super.onResume();
        if (this.hasFlash) {
            turnOnFlash();
        }
    }


    public void onStart() {
        super.onStart();
        getCamera();
    }


    public void onStop() {
        super.onStop();
        Camera camera = this.camera;
        if (camera != null) {
            camera.release();
            this.camera = null;
        }
    }

    public void checkFlash() {
        this.hasFlash = getApplicationContext().getPackageManager().hasSystemFeature("android.hardware.camera.flash");
        if (this.hasFlash) {
            Log.d("FFF", "c'è il flash");
            return;
        }
        Builder builder = new Builder(this);
        builder.setTitle("Errore!");
        builder.setMessage("Il tuo telefono non ha il flash!");
        builder.setPositiveButton("OK, compro un Nexus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VocalAlert.this.finish();
            }
        });
    }

    private void getCamera() {
        this.preview = (SurfaceView) findViewById(R.id.PREVIEW);
        this.mHolder = this.preview.getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setType(3);
        if (this.camera == null) {
            try {
                this.camera = Camera.open();
                this.params = this.camera.getParameters();
                try {
                    this.camera.setPreviewDisplay(this.mHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (RuntimeException unused) {
            }
        }
    }

    private void turnOnFlash() {
        if (!this.isFlashOn) {
            Camera camera = this.camera;
            if (camera != null && this.params != null) {
                this.isFlashOn = true;
                try {
                    this.params = camera.getParameters();
                    this.params.setFlashMode("torch");
                    this.camera.setParameters(this.params);
                    this.camera.startPreview();
                } catch (Exception unused) {
                }
            }
        }
    }

    private void turnOffFlash() {
        if (this.isFlashOn) {
            Camera camera = this.camera;
            if (camera != null && this.params != null) {
                this.isFlashOn = false;
                try {
                    this.params = camera.getParameters();
                    this.params.setFlashMode("off");
                    this.camera.setParameters(this.params);
                    this.camera.stopPreview();
                } catch (Exception unused) {
                }
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        CountDownTimer countDownTimer = this.mTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        turnOffFlash();
        MediaPlayer mediaPlayer = mySong;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        this.run = false;
        runvibrate(false);
        Intent intent = new Intent(this, GetStartActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setProgresseBarInvisible() {
        if (binding.progressBar.getVisibility() == View.VISIBLE) {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void loadAdsNative() {
        try {
            Admob.getInstance().loadNativeAd(VocalAlert.this, getString(R.string.native_alert), new NativeCallback() {
                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(VocalAlert.this).inflate(R.layout.layout_native_setting, null);
                    binding.frAds.removeAllViews();
                    binding.frAds.addView(adView);
                    Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
                }

                @Override
                public void onAdFailedToLoad() {
                    binding.frAds.removeAllViews();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            binding.frAds.removeAllViews();
        }
    }

    private void loadInter() {
        Admob.getInstance().loadInterAds(this, getString(R.string.inter_alert), new InterCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                Constant.interAlert = interstitialAd;
            }
        });
    }
}
