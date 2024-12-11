package com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.StartStopLockService;


public class HomeActivityVoice extends AppCompatActivity implements View.OnClickListener {
    public static int JOB_ID = 1001;
    public static final String TAG = "HomeActivityVoice";

    LinearLayout ll_pin;
    ImageView ll_settings;
    ImageView back_button;
    LinearLayout ll_voice_password;
    RelativeLayout root_layout;
    SwitchCompat sc_voiceLockService;
    StartStopLockService startStopLockService;


    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home_voice);

        initWidgets();
        this.startStopLockService = new StartStopLockService();
        getSharedPreferences("INITIALIZE", 0).edit().putBoolean("initialization_success", true).apply();
//        Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.bg)).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
//                return true;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
//                HomeActivityVoice.this.root_layout.setBackground(drawable);
//                return true;
//            }
//        }).submit();
        this.back_button.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        checkDrawOverOtherAppsPermission(this);
        if (getSharedPreferences("voice_recognition_preference", 0).getBoolean("lock_service", true)) {
            this.startStopLockService.startServiceForAllAndroidAPILevel(this);
        }
        this.sc_voiceLockService.setChecked(getSharedPreferences("voice_recognition_preference", 0).getBoolean("lock_service", true));
        this.sc_voiceLockService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    HomeActivityVoice.this.startStopLockService.startServiceForAllAndroidAPILevel(HomeActivityVoice.this);
                } else {
                    HomeActivityVoice.this.startStopLockService.stopServiceInAllAPILevel(HomeActivityVoice.this);
                }
            }
        });
    }

    private void initWidgets() {
        this.root_layout = (RelativeLayout) findViewById(R.id.home_activity_root_layout);

        this.ll_pin = (LinearLayout) findViewById(R.id.ll_keypad_pin);
        this.ll_settings = (ImageView) findViewById(R.id.ll_settings);
        this.back_button = (ImageView) findViewById(R.id.back_button);
        this.ll_voice_password = (LinearLayout) findViewById(R.id.ll_voice_password);
        this.sc_voiceLockService = (SwitchCompat) findViewById(R.id.voice_lock_service);
//        this.ll_rate = (ConstraintLayout) findViewById(R.id.ll_rate);
//        this.ll_share = (ConstraintLayout) findViewById(R.id.ll_share);
//        this.ll_policy = (ConstraintLayout) findViewById(R.id.ll_policy);

//
//        this.ll_rate.setOnClickListener(this);
//        this.ll_share.setOnClickListener(this);
//        this.ll_policy.setOnClickListener(this);
        this.ll_settings.setOnClickListener(this);
        this.ll_pin.setOnClickListener(this);
        this.ll_voice_password.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (getSharedPreferences("voice_recognition_preference", 0).getBoolean("lock_service", true)) {
            SwitchCompat switchCompat = this.sc_voiceLockService;
            if (switchCompat == null || switchCompat.isChecked()) {
                return;
            }
            this.sc_voiceLockService.setChecked(true);
            return;
        }
        SwitchCompat switchCompat2 = this.sc_voiceLockService;
        if (switchCompat2 == null || !switchCompat2.isChecked()) {
            return;
        }
        this.sc_voiceLockService.setChecked(false);
    }


    private void checkDrawOverOtherAppsPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                return;
            }
        }
        startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 0);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id == R.id.ll_keypad_pin){
            startActivity(new Intent(this, UpdateAlternatePinActivity.class));
        }else if(id == R.id.ll_settings){
            startActivity(new Intent(this, AppSettingsPreferenceActivity.class));
        }else if(id == R.id.ll_voice_password){
            startActivity(new Intent(this, UpdateVoicePasswordActivity.class));
        }

//        switch (view.getId()) {
//            case R.id.ll_keypad_pin:
//                startActivity(new Intent(this, UpdateAlternatePinActivity.class));
//                return;
//            case R.id.ll_set_alternate_pin:
//            case R.id.ll_set_voice_password:
//            default:
//                return;
//            case R.id.ll_settings:
//                return;
//            case R.id.ll_voice_password:
//                return;
//        }
    }

    public void rateToApplication() {
        String packageName = getPackageName();
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            intent.setPackage("com.android.vending");
            startActivity(intent);
        } catch (Exception unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public void shareApplication() {
        try {
            String packageName = getPackageName();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Take a look at \"" + getResources().getString(R.string.app_name) + "\"");
            StringBuilder sb = new StringBuilder();
            sb.append("https://play.google.com/store/apps/details?id=");
            sb.append(packageName);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(getApplicationContext(), "No Activity found to handle this Intent", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
