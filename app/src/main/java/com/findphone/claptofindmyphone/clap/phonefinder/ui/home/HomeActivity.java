package com.findphone.claptofindmyphone.clap.phonefinder.ui.home;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityHomeBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.battery.BatteryStatusActivity;

import com.findphone.claptofindmyphone.clap.phonefinder.ui.infor.InfoActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.setting.SettingFindPhoneActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.settingapp.SettingAppActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.PermissionUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SharePreferenceUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.voice_passcode.VoicePasscodeActivity;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

public class HomeActivity extends BaseActivity {

    ActivityHomeBinding binding;

    @Override
    public void bind() {
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPerMission();
        binding.frClap.setOnClickListener(v -> {
            if (PermissionUtils.cameraPermissionGrant(HomeActivity.this) && PermissionUtils.recordAudioPermissionGrant(this)) {
                startActivity(new Intent(HomeActivity.this, SettingFindPhoneActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, PermissionHome.class));
            }

        });
        binding.btnChangeAlarm.setOnClickListener(v -> {
            if (PermissionUtils.recordAudioPermissionGrant(this) &&
                    Settings.canDrawOverlays(this) && PermissionUtils.cameraPermissionGrant(this)) {
                startActivity(new Intent(HomeActivity.this, BatteryStatusActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, PermissionHome.class));
            }
        });
        binding.btnVoicepassCode.setOnClickListener(v -> {
            if (PermissionUtils.recordAudioPermissionGrant(this) && Settings.canDrawOverlays(this)) {
                startActivity(new Intent(HomeActivity.this, VoicePasscodeActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, PermissionHome.class));
            }
        });
        binding.imgSetting.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SettingAppActivity.class);
            v.getContext().startActivity(intent);
        });
        binding.infomation.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InfoActivity.class);
            v.getContext().startActivity(intent);
        });

        loadBanner();

    }

    private void checkPerMission() {
        if (PermissionUtils.recordAudioPermissionGrant(this) && PermissionUtils.cameraPermissionGrant(this)) {
            binding.bgClap.setVisibility(View.GONE);
        } else {
            binding.bgClap.setVisibility(View.VISIBLE);
        }
        if (PermissionUtils.recordAudioPermissionGrant(this) &&
                Settings.canDrawOverlays(this) && PermissionUtils.cameraPermissionGrant(this)) {
            binding.bgBattery.setVisibility(View.GONE);
        } else {
            binding.bgBattery.setVisibility(View.VISIBLE);
        }

        if (PermissionUtils.recordAudioPermissionGrant(this) && Settings.canDrawOverlays(this)) {
            binding.bgVoiPass.setVisibility(View.GONE);
        } else {
            binding.bgVoiPass.setVisibility(View.VISIBLE);
        }
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {

        }
    });

    int homeCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        checkPerMission();
        if (SharePreferenceUtils.isFirstHome(HomeActivity.this)) {
            homeCount++;

            Log.d("homeCount", "onResume: " + homeCount);

            if (homeCount > 1) {
                SharePreferenceUtils.setFirstHome(HomeActivity.this, false);
            }
        }

        loadNativeHome();

    }

    private void loadNativeHome() {
        if (SharePreferenceUtils.isOrganic(HomeActivity.this)) {

            Log.d("loadNativeHome", "loadNativeHome: " + SharePreferenceUtils.isFirstHome(HomeActivity.this));


            if (!SharePreferenceUtils.isFirstHome(HomeActivity.this)) {
                loadAds();

            } else {
                binding.frAds.setVisibility(View.GONE);
            }

        } else {
            loadAds();
        }
    }


    private void loadAds() {
        Admob.getInstance().loadNativeAd(this, getString(R.string.native_home), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                NativeAdView adView = (NativeAdView) LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_native_home, null);
                binding.frAds.setVisibility(View.VISIBLE);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void loadBanner() {
        Admob.getInstance().loadCollapsibleBanner(
                HomeActivity.this,
                getString(R.string.banner_collap),
                "bottom"
        );
    }
}
