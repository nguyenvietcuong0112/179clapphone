package com.findphone.claptofindmyphone.clap.phonefinder.ui.sound;

import android.media.MediaPlayer;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.GridLayoutManager;


import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivitySoundDetailBinding;
import com.findphone.claptofindmyphone.clap.phonefinder.model.SoundModel;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.base.BaseActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.sound.adapter.SoundAdapter;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Constant;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemConfiguration;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.SystemUtil;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

import java.util.List;
import java.util.Objects;

public class SoundActivity extends BaseActivity implements SoundAdapter.IClickSound, MediaPlayer.OnPreparedListener {

    private MediaPlayer player;
    private List<SoundModel> soundModels;
    private SoundAdapter soundAdapter;
    private int count = 0;
    ActivitySoundDetailBinding binding;

    @Override
    public void bind() {
        SystemUtil.setLocale(this);
        SystemConfiguration.setStatusBarColor(this, R.color.mycolorwhite, SystemConfiguration.IconColor.ICON_DARK);
        binding = ActivitySoundDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadAdsNative();
        soundModels = Constant.getSoundDefault(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        soundAdapter = new SoundAdapter(this, soundModels, this);
        binding.rvSound.setLayoutManager(gridLayoutManager);
        binding.rvSound.setAdapter(soundAdapter);
        binding.ivBackButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            stopPlaying();
        });
    }


    public void loadAdsNative() {
        try {
            Admob.getInstance().loadNativeAd(this, getString(R.string.native_sound), new NativeCallback() {
                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(SoundActivity.this).inflate(R.layout.layout_native_sound, null);
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

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    private void stopPlaying() {
        try {
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
                player = new MediaPlayer();
            }
        } catch (Exception ignored) {
        }
    }


    @Override
    public void onClick(SoundModel model) {
        stopPlaying();
        if (Objects.equals(model.getIsoSound(), "bird")) {
            player = MediaPlayer.create(this, R.raw.music_bird);
            player.start();
            player.setLooping(false);
        } else if (Objects.equals(model.getIsoSound(), "cats")) {
            player = MediaPlayer.create(this, R.raw.music_cat);
            player.start();
            player.setLooping(false);
        } else if (model.getIsoSound().equals("pig")) {
            player = MediaPlayer.create(this, R.raw.music_pig);
            player.start();
            player.setLooping(false);
        }else if(model.getIsoSound().equals("cow")) {
            player = MediaPlayer.create(this,R.raw.music_cow);
            player.start();
            player.setLooping(false);
        } else if (model.getIsoSound().equals("duck")) {
            player = MediaPlayer.create(this, R.raw.music_duck);
            player.start();
            player.setLooping(false);
        } else if (model.getIsoSound().equals("frog")) {
            player = MediaPlayer.create(this, R.raw.music_frog);
            player.start();
            player.setLooping(false);
        }else if(model.getIsoSound().equals("goat")) {
            player = MediaPlayer.create(this,R.raw.music_goat);
            player.start();
            player.setLooping(false);
        } else if (model.getIsoSound().equals("mouse")) {
            player = MediaPlayer.create(this, R.raw.music_mouse);
            player.start();
            player.setLooping(false);
        } else if (model.getIsoSound().equals("chicken")) {
            player = MediaPlayer.create(this, R.raw.music_chicken);
            player.start();
            player.setLooping(false);
        } else if (model.getIsoSound().equals("dog")) {
            player = MediaPlayer.create(this, R.raw.music_dog);
            player.start();
            player.setLooping(false);
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            int maxCount = 100;

            public void onCompletion(MediaPlayer mp) {
                if (count < maxCount) {
                    count++;
                    player.seekTo(0);
                    player.start();
                }
            }
        });
        SystemUtil.setIsoSound(this, model.getIsoSound());
    }


}
