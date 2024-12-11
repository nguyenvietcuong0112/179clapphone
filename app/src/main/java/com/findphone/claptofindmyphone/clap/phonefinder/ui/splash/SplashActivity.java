package com.findphone.claptofindmyphone.clap.phonefinder.ui.splash;

import android.content.Intent;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivitySplashBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.language.LanguageActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SharePreferenceUtils;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.google.firebase.database.FirebaseDatabase;
import com.mallegan.ads.callback.InterCallback;
import com.mallegan.ads.util.Admob;
import com.mallegan.ads.util.ConsentHelper;

import java.util.Map;

public class SplashActivity extends BaseActivity {
    private InterCallback interCallback;

    @Override
    public void bind() {
        SystemUtil.setLocale(this);
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
        ActivitySplashBinding activitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(activitySplashBinding.getRoot());
        loadAds();
    }

    private void loadAds() {
        interCallback = new InterCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                startActivity(new Intent(SplashActivity.this, LanguageActivity.class));
                finish();
            }
        };
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

        ConsentHelper consentHelper = ConsentHelper.getInstance(this);
        if (!consentHelper.canLoadAndShowAds()) {
            consentHelper.reset();
        }
        consentHelper.obtainConsentAndShow(this, () -> {
            Admob.getInstance().loadSplashInterAds2(SplashActivity.this, getString(R.string.inter_splash), 3000, interCallback);
            Constant.loadNativeLanguageSelect(SplashActivity.this);
        });
    }



}
