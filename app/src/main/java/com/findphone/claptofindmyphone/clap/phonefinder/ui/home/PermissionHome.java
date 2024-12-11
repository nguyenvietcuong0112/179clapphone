package com.findphone.claptofindmyphone.clap.phonefinder.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityRequestPermissionHomeBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.service.BatteryService;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.PermissionUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.ToastUtils;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

public class PermissionHome extends BaseActivity {

    private ActivityRequestPermissionHomeBinding binding;

    @Override
    public void bind() {
        SystemUtil.setLocale(this);
        SystemConfiguration.setStatusBarColor(this, R.color.mycolorwhite, SystemConfiguration.IconColor.ICON_DARK);
        binding = ActivityRequestPermissionHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtBack.setOnClickListener(v -> {
            finish();
            startService(new Intent(this, BatteryService.class));
        });
        binding.onOffSwitchRecord.setOnClickListener(v -> requestRecordAudioPermission());

        binding.onOffCamera.setOnCheckedChangeListener((compoundButton, z) -> {
            if(z){
                requestCamera();
            }

        });

        binding.onOffOverLay.setOnCheckedChangeListener((compoundButton, z) -> {
            if (z) {
                if (!Settings.canDrawOverlays(PermissionHome.this)) {
                    Intent intent = new Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName())
                    );
                    overlayPermissionLauncher.launch(intent);
                }
            }
        });
    }

    private ActivityResultLauncher<Intent> overlayPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (Settings.canDrawOverlays(PermissionHome.this)) {
                                binding.onOffOverLay.setChecked(true);
                                binding.onOffOverLay.setEnabled(false);
                                binding.onOffOverLay.setClickable(false);
                                ToastUtils.getInstance(PermissionHome.this)
                                        .showToast("Overlay permission has been granted.");
                            } else {
                                ToastUtils.getInstance(PermissionHome.this)
                                        .showToast("You need to grant overlay permission to continue.");
                            }
                        }
                    }
            );

    @Override
    protected void onResume() {
        super.onResume();
        checkPer();
        loadNativeAds();
    }

    private void checkPer() {
        if (PermissionUtils.cameraPermissionGrant(this)) {
            binding.onOffCamera.setChecked(true);
        }
        if (PermissionUtils.recordAudioPermissionGrant(this)) {
            binding.onOffSwitchRecord.setChecked(true);
        }
        if (Settings.canDrawOverlays(PermissionHome.this)) {
            binding.onOffOverLay.setChecked(true);
        }
    }

    private void requestRecordAudioPermission() {
        binding.onOffSwitchRecord.setChecked(true);
        requestPermissionLauncher.launch(PermissionUtils.RECORD_AUDIO_PERMISSION);
    }

    private void requestCamera() {
        binding.onOffCamera.setChecked(true);
        requestPermissionCameraLauncher.launch(PermissionUtils.CAMERA_PERMISSION);
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (PermissionUtils.recordAudioPermissionGrant(this)) {
                    binding.onOffSwitchRecord.setChecked(true);
                } else {


                    PermissionUtils.showAlertPermissionNotGrant(binding.getRoot(), this);
                    binding.onOffSwitchRecord.setChecked(false);
                }
            });

    private final ActivityResultLauncher<String[]> requestPermissionCameraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (PermissionUtils.cameraPermissionGrant(this)) {
                    binding.onOffCamera.setChecked(true);
                } else {
                    PermissionUtils.showAlertPermissionNotGrant(binding.getRoot(), this);
                    binding.onOffCamera.setChecked(false);
                }
            });

    private void loadNativeAds() {
        Admob.getInstance().loadNativeAd(this, getString(R.string.native_permission2), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                NativeAdView adView = (NativeAdView) LayoutInflater.from(PermissionHome.this).inflate(R.layout.layout_native_language, null);
                binding.frAds.removeAllViews();
                binding.frAds.addView(adView);
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
            }

            @Override
            public void onAdFailedToLoad() {
                super.onAdFailedToLoad();
                binding.frAds.setVisibility(View.GONE);
            }
        });
    }
}

