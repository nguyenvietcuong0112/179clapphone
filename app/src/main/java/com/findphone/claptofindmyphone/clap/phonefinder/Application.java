package com.findphone.claptofindmyphone.clap.phonefinder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.appsflyer.AppsFlyerConversionListener;

import com.findphone.claptofindmyphone.clap.phonefinder.ui.splash.SplashActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.MySpeechRecognizerSingleton;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SharePreferenceUtils;
import com.facebook.FacebookSdk;

import com.mallegan.ads.util.AdsApplication;
import com.mallegan.ads.util.AppOpenManager;
import com.mallegan.ads.util.AppsFlyer;

import java.util.List;
import java.util.Map;


public class Application extends AdsApplication {


    @Override
    public boolean enableAdsResume() {
        return true;
    }

    @Override
    public List<String> getListTestDeviceId() {
        return null;
    }

    @Override
    public String getResumeAdId() {
        return getString(R.string.open_resume);
    }

    @Override
    public Boolean buildDebug() {
        return null;
    }

    public static String CHANNEL_ID = "";


    public void onCreate() {
        super.onCreate();
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token));

//        if (!SharePreferenceUtils.isOrganic(getApplicationContext())) {
//            AppsFlyer.getInstance().initAppFlyer(this, getString(R.string.AF_DEV_KEY), true);
//
//        } else {
//            AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
//                @Override
//                public void onConversionDataSuccess(Map<String, Object> conversionData) {
//                    String mediaSource = (String) conversionData.get("media_source");
//
//                    SharePreferenceUtils.setOrganicValue(getApplicationContext(), mediaSource == null || mediaSource.isEmpty() || mediaSource.equals("organic"));
//                }
//
//                @Override
//                public void onConversionDataFail(String errorMessage) {
//                    // Handle conversion data failure
//                }
//
//                @Override
//                public void onAppOpenAttribution(Map<String, String> attributionData) {
//                    // Handle app open attribution
//                }
//
//                @Override
//                public void onAttributionFailure(String errorMessage) {
//                    // Handle attribution failure
//                }
//            };
//
//            AppsFlyer.getInstance().initAppFlyer(this, getString(R.string.AF_DEV_KEY), true, conversionListener);
//
//        }



        CHANNEL_ID = getPackageName();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "VOICE LOCK SERVICE", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton = MySpeechRecognizerSingleton.getInstance(this);
        mySpeechRecognizerSingleton.initSpeechRecognizer(this);
        mySpeechRecognizerSingleton.initRecognizerIntent(this);

    }


}



