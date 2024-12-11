package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;


import android.content.Context;
import android.media.MediaPlayer;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.model.LanguageModel;
import com.findphone.claptofindmyphone.clap.phonefinder.model.SoundModel;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static InterstitialAd interGetTwo = null;
    public static InterstitialAd interAlert = null;
    public static InterstitialAd interIntro = null;

    public static NativeAd nativeAdsLanguageSelect = null;


    public static void loadNativeLanguageSelect(Context context) {
        if (Constant.nativeAdsLanguageSelect == null) {
            Admob.getInstance().loadNativeAd(context, context.getString(R.string.native_language_select), new NativeCallback() {
                @Override
                public void onAdFailedToLoad() {
                    super.onAdFailedToLoad();
                    Constant.nativeAdsLanguageSelect = null;
                }

                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    super.onNativeAdLoaded(nativeAd);
                    Constant.nativeAdsLanguageSelect = nativeAd;
                }
            });
        }
    }

    public static ArrayList<LanguageModel> getLanguage() {
        ArrayList<LanguageModel> listLanguage = new ArrayList<>();
        listLanguage.add(new LanguageModel("English", "en", false, R.drawable.flag_en));
        listLanguage.add(new LanguageModel("Hindi", "hi", false, R.drawable.flag_hi));
        listLanguage.add(new LanguageModel("Spanish", "es", false, R.drawable.flag_es));
        listLanguage.add(new LanguageModel("French", "fr", false, R.drawable.flag_fr));
        listLanguage.add(new LanguageModel("German", "de", false, R.drawable.flag_de));
        listLanguage.add(new LanguageModel("Italia", "it", false, R.drawable.flag_italia));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false, R.drawable.flag_portugese));
        listLanguage.add(new LanguageModel("Korea", "ko", false, R.drawable.flag_korea));
        return listLanguage;
    }

    public static List<SoundModel> getSoundDefault(Context context) {
        List<SoundModel> soundModels = new ArrayList<>();
        soundModels.add(new SoundModel(R.drawable.bird, MediaPlayer.create(context, R.raw.music_bird), true, "bird"));
        soundModels.add(new SoundModel(R.drawable.cats, MediaPlayer.create(context, R.raw.music_cat), false, "cats"));
        soundModels.add(new SoundModel(R.drawable.pig, MediaPlayer.create(context, R.raw.music_pig), false, "pig"));
        soundModels.add(new SoundModel(R.drawable.cow, MediaPlayer.create(context, R.raw.music_cow), false, "cow"));
        soundModels.add(new SoundModel(R.drawable.dog, MediaPlayer.create(context, R.raw.music_dog), false, "dog"));
        soundModels.add(new SoundModel(R.drawable.duck, MediaPlayer.create(context, R.raw.music_duck), false, "duck"));
        soundModels.add(new SoundModel(R.drawable.frog, MediaPlayer.create(context, R.raw.music_frog), false, "frog"));
        soundModels.add(new SoundModel(R.drawable.goat, MediaPlayer.create(context, R.raw.music_goat), false, "goat"));
        soundModels.add(new SoundModel(R.drawable.mouse, MediaPlayer.create(context, R.raw.music_mouse), false, "mouse"));
        soundModels.add(new SoundModel(R.drawable.chicken, MediaPlayer.create(context, R.raw.music_chicken), false, "chicken"));
        return soundModels;
    }

}
