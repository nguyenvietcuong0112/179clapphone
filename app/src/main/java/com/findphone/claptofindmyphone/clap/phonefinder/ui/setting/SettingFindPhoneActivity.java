package com.findphone.claptofindmyphone.clap.phonefinder.ui.setting;


import android.app.Activity;
import android.app.ActivityManager;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;


import com.bumptech.glide.Glide;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivitySettingsBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.model.DefaultPreferences;
import com.findphone.claptofindmyphone.clap.phonefinder.model.SoundModel;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.getstart.GetStartActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.services.VocalService;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.sound.SoundActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.PermissionUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SharePreferenceUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.ToastUtils;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

import java.util.List;


public class SettingFindPhoneActivity extends BaseActivity {
    public static Activity activitySetting;
    public Boolean mPermCAm;
    private DefaultPreferences defaultPreferences;
    private String flashbox;
    private int intOnTick;
    public static List<SoundModel> soundModels;
    private String vibratebox;
    private ActivitySettingsBinding binding;
    private int selectedImg;
    private MediaPlayer mediaPlayer;
    private ImageView back_button;

    @Override
    public void bind() {
        SystemUtil.setLocale(this);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        soundModels = Constant.getSoundDefault(this);
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_LIGHT);
        activitySetting = this;
        if (binding.progressBar.getVisibility() == View.INVISIBLE) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        this.mPermCAm = Boolean.FALSE;
        binding.flashbox.setChecked(false);
        binding.vibratebox.setChecked(false);
        this.defaultPreferences = new DefaultPreferences(this);
        this.defaultPreferences.read("seekBar", "50");
        this.flashbox = this.defaultPreferences.read("flashbox", "1");
        this.vibratebox = this.defaultPreferences.read("vibratebox", "1");
        if (this.flashbox.equals("1")) {
            binding.flashbox.setChecked(true);
        }
        if (this.vibratebox.equals("1")) {
            binding.vibratebox.setChecked(true);
        }
        seekBarValue();
        initialization();
        loadAdsNative();
        binding.seekbar.setProgress(Integer.parseInt(this.defaultPreferences.read("seekBar", "50")));
        initvolume();
        setProgresseBarInvisible();
        new CountDownTimer(5000, 500) {
            public void onTick(long j) {
                SettingFindPhoneActivity.this.intOnTick = SettingFindPhoneActivity.this.intOnTick + 1;
                if (SettingFindPhoneActivity.this.intOnTick >= 9) {
                    SettingFindPhoneActivity.this.setProgresseBarInvisible();
                }
            }

            public void onFinish() {
                SettingFindPhoneActivity.this.setProgresseBarInvisible();
                SettingFindPhoneActivity.this.intOnTick = 0;
            }
        }.start();

        selectedImg = soundModels.get(0).getImageSound();

        checkCamFlash();

//        binding.settingButton.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), SettingAppActivity.class);
//            v.getContext().startActivity(intent);
//        });

        binding.seekbar.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), SoundActivity.class);
                    v.getContext().startActivity(intent);
                }
        );
//        binding.infomation.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), InfoActivity.class);
//            v.getContext().startActivity(intent);
//        });


        binding.buttonController.setOnClickListener(v -> {
            if (isMyServiceRunning(VocalService.class)) {
                binding.ivBackgroundClap.setImageResource(R.drawable.image_background_clap_off);
                binding.txtActive.setText("Tap to active");
                Glide.with(SettingFindPhoneActivity.this).asDrawable().load(selectedImg).into(binding.buttonController);
                stopService(new Intent(SettingFindPhoneActivity.this, VocalService.class));
                for (int i = 0; i < soundModels.size(); i++) {
                    if (soundModels.get(i).getSound() != null) {
                        mediaPlayer = soundModels.get(i).getSound();
                        mediaPlayer.start();
                        break;
                    }
                }
                ToastUtils.getInstance(this).showToast("Detection stopped");
                return;
            }
            binding.ivBackgroundClap.setImageResource(R.drawable.image_background_clap_on);
            binding.txtActive.setText("Tap to deactivate");
            Glide.with(SettingFindPhoneActivity.this).asDrawable().load(selectedImg).into(binding.buttonController);
            check();
        });
        binding.next.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SoundActivity.class);
            v.getContext().startActivity(intent);
        });

        this.back_button = (ImageView) findViewById(R.id.back_button);
        this.back_button.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }


    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void seekBarValue() {
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String stringBuilder = "" + i;
                SettingFindPhoneActivity.this.defaultPreferences.save("seekBar", stringBuilder);
                SettingFindPhoneActivity.this.setvolume(i);
            }
        });
    }

    private void checkbox() {
        if (!binding.flashbox.isChecked()) {
            this.defaultPreferences.save("flashbox", "0");
        } else if (this.mPermCAm) {
            this.defaultPreferences.save("flashbox", "1");
        } else {
            this.defaultPreferences.save("flashbox", "0");
        }
        if (binding.vibratebox.isChecked()) {
            this.defaultPreferences.save("vibratebox", "1");
        } else {
            this.defaultPreferences.save("vibratebox", "0");
        }
    }

    private void initvolume() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.getStreamVolume(3);
        audioManager.setStreamVolume(3, (int) (((float) audioManager.getStreamMaxVolume(3)) * (Float.parseFloat(this.defaultPreferences.read(NotificationCompat.CATEGORY_PROGRESS, "50")) / 100.0f)), 0);
    }

    private void setvolume(int i) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(3, (int) (((float) audioManager.getStreamMaxVolume(3)) * (((float) i) / 100.0f)), 0);
    }

    private void initialization() {

    }

    public void checkCamFlash() {
        if (!PermissionUtils.cameraPermissionGrant(this)) {
            requestPermissionCameraLauncher.launch(PermissionUtils.CAMERA_PERMISSION);
            return;
        }
        this.mPermCAm = Boolean.TRUE;
    }

    public void check() {
        if (!PermissionUtils.recordAudioPermissionGrant(this)) {
            requestPermissionLauncher.launch(PermissionUtils.RECORD_AUDIO_PERMISSION);
            return;
        }
        initializePlayerAndStartRecording();
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (PermissionUtils.recordAudioPermissionGrant(this)) {
                    initializePlayerAndStartRecording();
                } else {
                    PermissionUtils.showAlertPermissionNotGrant(binding.getRoot(), this);
                }
            });

    private final ActivityResultLauncher<String[]> requestPermissionCameraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (PermissionUtils.recordAudioPermissionGrant(this)) {
                    this.mPermCAm = Boolean.TRUE;
                } else {
                    PermissionUtils.showAlertPermissionNotGrant(binding.getRoot(), this);
                    this.mPermCAm = Boolean.FALSE;
                }
            });


    @Override
    protected void onResume() {
        super.onResume();
        String isoSound = SystemUtil.getIsoSound(this);
        for (int i = 0; i < soundModels.size(); i++) {
            if (soundModels.get(i).getIsoSound().equals(isoSound)) {
                selectedImg = soundModels.get(i).getImageSound();
                SharePreferenceUtils.getInstance(this).setSetSound(selectedImg);
                break;
            }
        }
        if (isMyServiceRunning(VocalService.class)) {
            binding.ivBackgroundClap.setImageResource(R.drawable.image_background_clap_on);
        } else {
            binding.ivBackgroundClap.setImageResource(R.drawable.image_background_clap_off);
        }
        Glide.with(SettingFindPhoneActivity.this).asDrawable().load(selectedImg).into(binding.buttonController);
    }


    private void initializePlayerAndStartRecording() {
        checkbox();
        this.defaultPreferences.save("StopService", "0");
        startService(new Intent(getBaseContext(), VocalService.class));
        Toast.makeText(this, "Detection started", Toast.LENGTH_LONG).show();

        if (GetStartActivity.MainIsRun) {
            try {
                GetStartActivity.activityGetStart.finish();
            } catch (Exception ignored) {
            }
        }
    }


    private void setProgresseBarInvisible() {
        if (binding.progressBar.getVisibility() == View.VISIBLE) {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void loadAdsNative() {
        try {
            Admob.getInstance().loadNativeAd(SettingFindPhoneActivity.this, getString(R.string.native_home), new NativeCallback() {
                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(SettingFindPhoneActivity.this).inflate(R.layout.layout_native_setting, null);
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

    public void onDestroy() {
        super.onDestroy();
    }

}
