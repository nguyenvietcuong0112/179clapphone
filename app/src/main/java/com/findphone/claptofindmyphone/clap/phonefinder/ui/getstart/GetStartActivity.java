package com.findphone.claptofindmyphone.clap.phonefinder.ui.getstart;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityGetStartBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.setting.SettingFindPhoneActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.InterCallback;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

public class GetStartActivity extends AppCompatActivity {

    public static boolean MainIsRun;
    public static Activity activityGetStart;
    private ActivityGetStartBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SystemConfiguration.setStatusBarColor(this, R.color.mycolorwhite, SystemConfiguration.IconColor.ICON_DARK);
        activityGetStart = this;
        MainIsRun = true;
        isMyServiceRunning(VocalService.class);
        loadAdsNative();
        loadInter();
        binding.start.setOnClickListener(v -> Admob.getInstance().showInterAds(GetStartActivity.this, Constant.interGetTwo,new InterCallback(){
            @Override
            public void onNextAction() {
                super.onNextAction();
                Intent intent = new Intent(GetStartActivity.this, SettingFindPhoneActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }));
        binding.rate.setOnClickListener(v->{
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.finder.clap.findphone.whistle.byclapping")));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.finder.clap.findphone.whistle.byclapping")));
            }
        });
        binding.share.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", getString(R.string.app_name) + " app for Android - https://play.google.com/store/apps/details?id=com.finder.clap.findphone.whistle.byclapping");
            startActivity(Intent.createChooser(intent, "Share to"));
        });
        binding.privacy.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://zerogapps.netlify.app/policy"));
            startActivity(intent);
        });
    }

    private void loadAdsNative() {
        try {
            Admob.getInstance().loadNativeAd(GetStartActivity.this, getString(R.string.native_get_two), new NativeCallback() {
                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(GetStartActivity.this).inflate(R.layout.layout_native_get_two, null);
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

    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void loadInter() {
        Admob.getInstance().loadInterAds(this, getString(R.string.inter_get_two), new InterCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                Constant.interGetTwo = interstitialAd;
            }
        });
    }

    private class VocalService {
    }
}
