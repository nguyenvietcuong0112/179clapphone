package com.findphone.claptofindmyphone.clap.phonefinder.ui.voice_passcode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.fragments.DisabledMicPermissionDialogFragments;
import com.findphone.claptofindmyphone.clap.phonefinder.fragments.RequestMicPermissionDialogFragments;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice.HomeActivityVoice;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice.SetNewAlternatePinActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice.SetNewVoicePasswordActivity;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Permissions;


public class VoicePasscodeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int SET_OVERLAY_PERMISSION_REQUEST_CODE = 1000;
    public static final int SET_PIN_PASSWORD_REQUEST_CODE = 900;
    public static final int SET_VOICE_PASSWORD_REQUEST_CODE = 800;
    public static final String TAG = "VoicePasscodeActivity";
    LottieAnimationView anim_screen_overlay_permission_done;
    LottieAnimationView anim_set_alternate_pin_done;
    LottieAnimationView anim_set_voice_password_done;
    LinearLayout btn_enabble_screen_overlay;
    AppCompatButton btn_get_started;
    LinearLayout btn_set_alternate_pin;
    LinearLayout btn_set_voice_pass;
    LinearLayout root_layout;

    ImageView back_button;
    boolean is_voice_password_setup_completed = false;
    boolean is_alternate_pin_setup_completed = false;
    boolean is_screen_overlay_permission_completed = false;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_initial_setup);

        initViews();
        back_button.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        String string = getSharedPreferences("PASSWORDS", 0).getString("user_voice_password", "");
        if (string != null && !string.equalsIgnoreCase("") && string.length() > 0) {
            this.anim_set_voice_password_done.playAnimation();
            this.anim_set_voice_password_done.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    VoicePasscodeActivity.this.anim_set_voice_password_done.setProgress(1.0f);
                    VoicePasscodeActivity.this.is_voice_password_setup_completed = true;
                    VoicePasscodeActivity.this.show_get_started_button_if_initialization_completed();
                }
            });
        }
        String string2 = getSharedPreferences("PASSWORDS", 0).getString("user_pin", "");
        if (string2 != null && !string2.equalsIgnoreCase("")) {
            this.anim_set_alternate_pin_done.playAnimation();
            this.anim_set_alternate_pin_done.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    VoicePasscodeActivity.this.anim_set_alternate_pin_done.setProgress(1.0f);
                    VoicePasscodeActivity.this.is_alternate_pin_setup_completed = true;
                    VoicePasscodeActivity.this.show_get_started_button_if_initialization_completed();
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                this.anim_screen_overlay_permission_done.playAnimation();
                this.anim_screen_overlay_permission_done.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        VoicePasscodeActivity.this.anim_screen_overlay_permission_done.setProgress(1.0f);
                        VoicePasscodeActivity.this.is_screen_overlay_permission_completed = true;
                        VoicePasscodeActivity.this.show_get_started_button_if_initialization_completed();
                    }
                });
            }
        }
        show_get_started_button_if_initialization_completed();
    }

    void initViews() {
        this.btn_enabble_screen_overlay = (LinearLayout) findViewById(R.id.ll_enable_screen_overlay);
        this.btn_set_alternate_pin = (LinearLayout) findViewById(R.id.ll_set_alternate_pin);
        this.btn_set_voice_pass = (LinearLayout) findViewById(R.id.ll_set_voice_password);
        this.btn_get_started = (AppCompatButton) findViewById(R.id.btn_get_started);
        this.root_layout = (LinearLayout) findViewById(R.id.initial_setup_activity_root_layout);
        this.anim_set_voice_password_done = (LottieAnimationView) findViewById(R.id.anim_voice_password_done);
        this.anim_set_alternate_pin_done = (LottieAnimationView) findViewById(R.id.anim_set_pin_done);
        this.anim_screen_overlay_permission_done = (LottieAnimationView) findViewById(R.id.anim_screen_overlay_permission_done);
        this.back_button = (ImageView) findViewById(R.id.back_button);
        this.btn_set_voice_pass.setOnClickListener(this);
        this.btn_set_alternate_pin.setOnClickListener(this);
        this.btn_enabble_screen_overlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id == R.id.btn_get_started){
            startActivity(new Intent(this, HomeActivityVoice.class));
            finish();
        }else if(id == R.id.ll_enable_screen_overlay){
            Log.e(TAG, "Screen overlay clicked");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 1000);
                        return;
                    } else {
                        Toast.makeText(this, "Enable \"DRAW OVER OTHER APPS\" from settings", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            Toast.makeText(this, "You already have \"DRAW_OVER_OTHER_APPS\" Permission for this app", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.ll_set_alternate_pin){
            Log.e(TAG, "Set alternate pin clicked");
            startActivityForResult(new Intent(this, SetNewAlternatePinActivity.class), 900);
        }else if(id == R.id.ll_set_voice_password){
            Log.e(TAG, "Set voice password clicked");
            if (!Permissions.haveMicPermission(this)) {
                show_request_permission_dialog();
                return;
            } else {
                startActivityForResult(new Intent(this, SetNewVoicePasswordActivity.class), 800);
                return;
            }
        }
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.e(TAG, "On Activity Result Request Code is - " + i);
        if (i == 777) {
            if (Permissions.haveMicPermission(this)) {
                startActivityForResult(new Intent(this, SetNewVoicePasswordActivity.class), 800);
            } else {
                show_disabled_permission_dialog();
            }
        } else if (i == 800) {
            if (i2 == -1) {
                this.anim_set_voice_password_done.playAnimation();
                this.anim_set_voice_password_done.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        VoicePasscodeActivity.this.is_voice_password_setup_completed = true;
                        VoicePasscodeActivity.this.show_get_started_button_if_initialization_completed();
                    }
                });
            }
        } else if (i == 900) {
            if (i2 == -1) {
                this.anim_set_alternate_pin_done.playAnimation();
                this.anim_set_alternate_pin_done.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        VoicePasscodeActivity.this.is_alternate_pin_setup_completed = true;
                        VoicePasscodeActivity.this.show_get_started_button_if_initialization_completed();
                    }
                });
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (i == 1000 && Settings.canDrawOverlays(this)) {
                this.anim_screen_overlay_permission_done.playAnimation();
                this.anim_screen_overlay_permission_done.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        VoicePasscodeActivity.this.is_screen_overlay_permission_completed = true;
                        VoicePasscodeActivity.this.show_get_started_button_if_initialization_completed();
                    }
                });
            }
        }
    }

    boolean isVoicePasscodeActivityCompleted() {
        return this.is_voice_password_setup_completed && this.is_alternate_pin_setup_completed && this.is_screen_overlay_permission_completed;
    }

    void show_get_started_button_if_initialization_completed() {
        if (isVoicePasscodeActivityCompleted() && this.btn_get_started.getVisibility() == View.INVISIBLE) {
            this.btn_get_started.setVisibility(View.VISIBLE);
            this.btn_get_started.setOnClickListener(this);
        }
    }

    private void show_request_permission_dialog() {
        new RequestMicPermissionDialogFragments().show(getSupportFragmentManager(), "mic_permission_dialog");
    }

    private void show_disabled_permission_dialog() {
        new DisabledMicPermissionDialogFragments().show(getSupportFragmentManager(), "disabled_mic_permission");
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 777) {
            if (Permissions.haveMicPermission(this)) {
                startActivityForResult(new Intent(this, SetNewVoicePasswordActivity.class), 800);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Permissions.micPermission)) {
                ActivityCompat.requestPermissions(this, Permissions._micPermisson, Permissions.MIC_PERMISSION_REQUEST_CODE);
            } else {
                show_disabled_permission_dialog();
            }
        }
    }
}
