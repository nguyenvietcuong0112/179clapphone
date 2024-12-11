package com.findphone.claptofindmyphone.clap.phonefinder.ui.language;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;


import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityLanguageBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.intro.IntroActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.language.adapter.LanguageStartAdapter;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SharePreferenceUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

import java.util.Map;

public class LanguageActivity extends BaseActivity {

    String codeLang = "en";
    LanguageStartAdapter languageAdapter;
    ActivityLanguageBinding binding;

    boolean isNativeLanguageSelectLoaded = false;

    @Override
    public void bind() {
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
        SystemUtil.setLocale(this);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (SharePreferenceUtils.isOrganic(this)) {
            AppsFlyerLib.getInstance().registerConversionListener(this, new AppsFlyerConversionListener() {

                @Override
                public void onConversionDataSuccess(Map<String, Object> conversionData) {
                    String mediaSource = (String) conversionData.get("media_source");
                    SharePreferenceUtils.setOrganicValue(getApplicationContext(), mediaSource == null || mediaSource.isEmpty() || mediaSource.equals("organic"));
                }

                @Override
                public void onConversionDataFail(String s) {
                    // Handle conversion data failure
                }

                @Override
                public void onAppOpenAttribution(Map<String, String> map) {
                    // Handle app open attribution
                }

                @Override
                public void onAttributionFailure(String s) {
                    // Handle attribution failure
                }
            });
        }


        languageAdapter = new LanguageStartAdapter(this, Constant.getLanguage(), data -> {
            codeLang = data.getIsoLanguage();
            binding.ivSelect.setVisibility(View.VISIBLE);
            binding.ivSelect.setAlpha(1.0f);
            binding.ivSelect.setSelected(true);
//            SharedPreferences sharedPreferences = getSharedPreferences("language_select", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            boolean isFirstLoad = sharedPreferences.getBoolean("isFirstLoadAds", true);
//            if (isFirstLoad) {
//                loadAdsNativeLanguageSelect();
//                editor.putBoolean("isFirstLoadAds", false);
//                editor.apply();
//            }

            if (!isNativeLanguageSelectLoaded) {
                loadAdsNativeLanguageSelect();
            }
        });
        binding.rvLanguage.setAdapter(languageAdapter);
        binding.ivSelect.setOnClickListener(v -> {
            SystemUtil.saveLocale(this, codeLang);
            SharePreferenceUtils.getInstance(this).setTutorial(true);
            startActivity(new Intent(LanguageActivity.this, IntroActivity.class));
        });
        binding.ivSelect.setVisibility(View.GONE);
        loadAds();
    }

    private void loadAds() {
        checkNextButtonStatus(false);
        Admob.getInstance().loadNativeAd(LanguageActivity.this, getString(R.string.native_language), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                NativeAdView adView = new NativeAdView(LanguageActivity.this);
                if (!SharePreferenceUtils.isOrganic(LanguageActivity.this)) {
                    adView = (NativeAdView) LayoutInflater.from(LanguageActivity.this).inflate(R.layout.layout_native_language_non_organic, null);
                } else {
                    adView = (NativeAdView) LayoutInflater.from(LanguageActivity.this).inflate(R.layout.layout_native_language, null);
                }
                binding.frAds.removeAllViews();
                binding.frAds.addView(adView);
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
                checkNextButtonStatus(true);
            }

            @Override
            public void onAdFailedToLoad() {
                super.onAdFailedToLoad();
                binding.frAds.removeAllViews();
                checkNextButtonStatus(true);
            }

        });
    }

    public void loadAdsNativeLanguageSelect() {
        NativeAdView adView;
        if (SharePreferenceUtils.isOrganic(this)) {
            adView = (NativeAdView) LayoutInflater.from(this)
                    .inflate(R.layout.layout_native_language, null);
        } else {
            adView = (NativeAdView) LayoutInflater.from(this)
                    .inflate(R.layout.layout_native_language_non_organic, null);
        }
        checkNextButtonStatus(false);
        Admob.getInstance().loadNativeAd(LanguageActivity.this, getString(R.string.native_language_select), new NativeCallback() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                isNativeLanguageSelectLoaded = true;
                binding.frAds.removeAllViews();
                binding.frAds.addView(adView);
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
                checkNextButtonStatus(true);
            }

            @Override
            public void onAdFailedToLoad() {
                binding.frAds.removeAllViews();
                checkNextButtonStatus(true);
            }
        });
    }


    private void checkNextButtonStatus(boolean isReady) {
        if (isReady) {
            binding.ivSelect.setVisibility(View.VISIBLE);
            binding.btnNextLoading.setVisibility(View.GONE);
        } else {
            binding.ivSelect.setVisibility(View.GONE);
            binding.btnNextLoading.setVisibility(View.VISIBLE);
        }
    }
}
