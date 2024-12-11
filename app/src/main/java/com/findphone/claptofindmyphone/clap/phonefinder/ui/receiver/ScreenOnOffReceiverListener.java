package com.findphone.claptofindmyphone.clap.phonefinder.ui.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.OnPasswordMatchedListener;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.LockScreenUI;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.VibrateAndVoice;


public class ScreenOnOffReceiverListener extends BroadcastReceiver implements OnPasswordMatchedListener {
    public static final String TAG = "ScreenOnOffReceiver";
    Context context;
    LockScreenUI lockScreenUI;
    ViewGroup lockView;
    ViewGroup pinCodeView;
    WindowManager windowManager;

    public ScreenOnOffReceiverListener(Context context) {
        this.context = context;
        LockScreenUI lockScreenUI = new LockScreenUI((Application) context.getApplicationContext());
        this.lockScreenUI = lockScreenUI;
        lockScreenUI.setOnPasswordMatchedListener(this);
        this.lockView = (ViewGroup) this.lockScreenUI.getLockScreenView();
        this.pinCodeView = (ViewGroup) this.lockScreenUI.getPinCodeView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.e(TAG, intent.getAction() + " Broadcast received");
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("android.intent.action.SCREEN_ON")) {
            Log.e(TAG, "SCREEN_ON Broadcast received");
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            this.windowManager = windowManager;
            if (windowManager != null) {
                if (!ViewCompat.isAttachedToWindow(this.lockView) && this.lockView.getWindowToken() == null) {
                    this.lockScreenUI.setupLockScreen((Application) context.getApplicationContext());
                    if (Settings.canDrawOverlays(context)) {
                        this.windowManager.addView(this.lockView, getLayoutParams());
                        Log.e(TAG, " windowManager.addView() called : Lock Screen Added");
                    } else {
                        Log.e(TAG, "Draw overlay permission not enabled");
                    }
                }
                this.lockScreenUI.startVoiceRecognizing((Application) context.getApplicationContext());
                VibrateAndVoice.vibrate(context.getApplicationContext());
                ((ImageView) this.lockView.findViewById(R.id.switch_to_key)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ScreenOnOffReceiverListener.this.lockView.findViewById(R.id.root_layout_enter_pincode) == null) {
                            if (ScreenOnOffReceiverListener.this.pinCodeView.getWindowToken() == null) {
                                ScreenOnOffReceiverListener.this.lockView.addView(ScreenOnOffReceiverListener.this.pinCodeView, -1, -1);
                                Log.e(ScreenOnOffReceiverListener.TAG, "Pincode View attached");
                                return;
                            }
                            return;
                        }
                        Log.e(ScreenOnOffReceiverListener.TAG, "Pincode View already attached");
                    }
                });
                ((ImageView) this.pinCodeView.findViewById(R.id.iv_icon_back)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScreenOnOffReceiverListener screenOnOffReceiverListener = ScreenOnOffReceiverListener.this;
                        screenOnOffReceiverListener.removePinCodeView(screenOnOffReceiverListener.pinCodeView, ScreenOnOffReceiverListener.this.lockView);
                    }
                });
                return;
            }
            Log.e(TAG, "Window manager is null");
        } else if (intent.getAction() == null || !intent.getAction().equalsIgnoreCase("android.intent.action.SCREEN_OFF")) {
        } else {
            Log.e(TAG, "SCREEN_OFF Broadcast received");
            this.lockScreenUI.stopVoiceRecognizing();
            this.lockScreenUI.cancelVoiceRecognizing();
            this.lockScreenUI.destroyVoiceRecognizing();
            ViewGroup viewGroup = this.pinCodeView;
            if (viewGroup != null) {
                if (viewGroup.getWindowToken() != null) {
                    this.lockView.removeView(this.pinCodeView);
                    return;
                }
                return;
            }
            Log.e(TAG, "Can't Remove PIN Code View beacause it's Null");
        }
    }

    public WindowManager.LayoutParams getLayoutParams() {
        if (Build.VERSION.SDK_INT >= 26) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(1024, 1024, 2038, 256, -3);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.width = -1;
            layoutParams.height = -1;
            layoutParams.screenBrightness = -1.0f;
            layoutParams.buttonBrightness = -1.0f;
            return layoutParams;
        }
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(1024, 1024, 2010, 256, -3);
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        layoutParams2.gravity = Gravity.CENTER;
        layoutParams2.screenBrightness = -1.0f;
        layoutParams2.buttonBrightness = -1.0f;
        return layoutParams2;
    }

    @Override
    public void onVoicePasswordMatched() {
        Log.e(TAG, "onVoicePasswordMatched called");
        removeLockScreen();
    }

    @Override
    public void onVoicePasswordNotMatched() {
        Log.e(TAG, "onVoicePasswordMatched called");
        if (this.lockView.isShown()) {
            VibrateAndVoice.invalidPassword(this.context.getApplicationContext());
            VibrateAndVoice.vibrate(this.context.getApplicationContext());
        }
    }

    @Override
    public void onPINPasswordMatched() {
        Log.e(TAG, "onPINPasswordMatched called");
        removeLockScreen();
    }

    private void removeLockScreen() {
        ViewGroup viewGroup = this.lockView;
        if (viewGroup != null && ViewCompat.isAttachedToWindow(viewGroup)) {
            if (this.lockView.isShown()) {
                VibrateAndVoice.passwordAccepted(this.context.getApplicationContext());
                VibrateAndVoice.vibrate(this.context.getApplicationContext());
            }
            if (this.lockView.getWindowToken() != null) {
                ViewGroup viewGroup2 = this.pinCodeView;
                if (viewGroup2 != null) {
                    if (viewGroup2.getWindowToken() != null) {
                        this.lockView.removeView(this.pinCodeView);
                    }
                } else {
                    Log.e(TAG, "Can't Remove PIN Code View beacause it's Null");
                }
                this.windowManager.removeView(this.lockView);
            }
            Log.e(TAG, "removeView LockView done from window manager");
            return;
        }
        Log.e(TAG, "lockView is null or Not Attached to window");
    }


    public void removePinCodeView(View view, ViewGroup viewGroup) {
        if (view != null) {
            if (viewGroup.findViewById(R.id.root_layout_enter_pincode) != null) {
                viewGroup.removeView(view);
                Log.e(TAG, "removeViewImmediate done");
                return;
            }
            return;
        }
        Log.e(TAG, "lockView is null or Not Attached to window");
    }
}
