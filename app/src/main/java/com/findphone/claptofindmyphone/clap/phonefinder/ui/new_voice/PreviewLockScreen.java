package com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.MySpeechRecognizerSingleton;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.VibrateAndVoice;


import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class PreviewLockScreen extends AppCompatActivity {
    public static final String TAG = "LockScreenUI";
    static MySpeechRecognizerSingleton mySpeechRecognizerSingleton;
    static Intent recognizerIntent;
    static SpeechRecognizer speechRecognizer;
    int[] backgroundsIDLockScreen = {R.drawable.bg0, R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4};
    ImageView iv_icon_back;
    AppCompatImageView iv_lockscreen_bg;
    ViewGroup keypad_password;
    LottieAnimationView mic_anim;
    RelativeLayout root_layout_lock_screen;
    SharedPreferences sharedPreferences;
    ImageView switch_to_keypad;
    TextView text_hint;
    TextView tv_date;
    TextView tv_time;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.sharedPreferences = getSharedPreferences("voice_recognition_preference", 0);
        setContentView(R.layout.lock_screen_view);


        setupLockScreen(getApplication());
        this.keypad_password = (ViewGroup) getPinCodeView(this);
        this.switch_to_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ViewCompat.isAttachedToWindow(PreviewLockScreen.this.keypad_password)) {
                    PreviewLockScreen.this.root_layout_lock_screen.addView(PreviewLockScreen.this.keypad_password);
                    Log.e("LockScreenUI", "Keypad password View Added to root layout !");
                    return;
                }
                Log.e("LockScreenUI", "Keypad password View Already Added !");
            }
        });
        ImageView imageView = (ImageView) this.keypad_password.findViewById(R.id.iv_icon_back);
        this.iv_icon_back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ViewCompat.isAttachedToWindow(PreviewLockScreen.this.keypad_password)) {
                    PreviewLockScreen.this.root_layout_lock_screen.removeView(PreviewLockScreen.this.keypad_password);
                }
            }
        });
    }

    private void voiceRecognizingSetup(Context context) {
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2 = MySpeechRecognizerSingleton.getInstance((Application) context.getApplicationContext());
        mySpeechRecognizerSingleton = mySpeechRecognizerSingleton2;
        speechRecognizer = mySpeechRecognizerSingleton2.getSpeechRecognizer((Application) context.getApplicationContext());
        recognizerIntent = mySpeechRecognizerSingleton.getRecognizerIntent((Application) context.getApplicationContext());
    }


    public void startVoiceRecognizing(Application application) {
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2;
        if (speechRecognizer != null && recognizerIntent != null && (mySpeechRecognizerSingleton2 = mySpeechRecognizerSingleton) != null) {
            if (!mySpeechRecognizerSingleton2.isListening()) {
                try {
                    speechRecognizer.startListening(recognizerIntent);
                    mySpeechRecognizerSingleton.setListening(true);
                    Log.e("LockScreenUI", "startVoiceRecognizing() : islistening value is " + mySpeechRecognizerSingleton.isListening());
                    speechRecognizer.setRecognitionListener(new MySpeechRecognizationListener(application));
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Log.e("LockScreenUI", "Security Exception occured while start listening called from speechRecognizer object");
                }
            } else {
                Log.e("LockScreenUI", "startVoiceRecognizing() speech recognizer already running : islistening value is " + mySpeechRecognizerSingleton.isListening());
            }
        }
        Log.e("LockScreenUI", "Speech Recognization startVoiceRecognizing Called !");
    }

    private void stopVoiceRecognizing() {
        SpeechRecognizer speechRecognizer2 = speechRecognizer;
        if (speechRecognizer2 != null) {
            speechRecognizer2.stopListening();
            Log.e("LockScreenUI", "Speech Recognization stopVoiceRecognizing Called !");
        }
    }

    private void initWidgets() {
        this.mic_anim = (LottieAnimationView) findViewById(R.id.id_lav_record_audio);
        this.text_hint = (TextView) findViewById(R.id.tv_all_toast);
        this.tv_date = (TextView) findViewById(R.id.tv_date);
        this.tv_time = (TextView) findViewById(R.id.tv_time);
        this.root_layout_lock_screen = (RelativeLayout) findViewById(R.id.root_layout_lock_screen);
        this.iv_lockscreen_bg = (AppCompatImageView) findViewById(R.id.lock_screen_background_imageview);
        this.switch_to_keypad = (ImageView) findViewById(R.id.switch_to_key);
    }

    public void setupLockScreen(final Application application) {
        initWidgets();
        voiceRecognizingSetup(application);
        setLockScreenBacground(application);
        setLockScreenFontStyle(application);
        showTimeOnLockScreen();
        this.mic_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewLockScreen.this.startVoiceRecognizing(application);
            }
        });
    }


    @Override
    public void onResume() {
        startVoiceRecognizing(getApplication());
        super.onResume();
    }


    @Override
    public void onPause() {
        stopVoiceRecognizing();
        super.onPause();
    }

    private void setLockScreenBacground(Context context) {
        String string = this.sharedPreferences.getString("select_background", "0");
        if (string != null) {
            Glide.with(context).load(Integer.valueOf(this.backgroundsIDLockScreen[Integer.parseInt(string)])).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            PreviewLockScreen.this.iv_lockscreen_bg.setBackground(new ColorDrawable(Color.parseColor("#2e2e2e")));
                        }
                    });
                    return true;
                }

                @Override
                public boolean onResourceReady(final Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            PreviewLockScreen.this.iv_lockscreen_bg.setImageDrawable(drawable);
                        }
                    });
                    return true;
                }
            }).submit();
        } else {
            Log.e("LockScreenUI", "select_background index is null");
        }
    }

    private void setLockScreenFontStyle(final Context context) {
        String string = this.sharedPreferences.getString("select_font", "0");
        if (string != null) {
            string.hashCode();
            char c = 65535;
            switch (string.hashCode()) {
                case 48:
                    if (string.equals("0")) {
                        c = 0;
                        break;
                    }
                    break;
                case 49:
                    if (string.equals("1")) {
                        c = 1;
                        break;
                    }
                    break;
                case 50:
                    if (string.equals("2")) {
                        c = 2;
                        break;
                    }
                    break;
                case 51:
                    if (string.equals("3")) {
                        c = 3;
                        break;
                    }
                    break;
                case 52:
                    if (string.equals("4")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "font1.ttf");
                            PreviewLockScreen.this.tv_date.setTypeface(createFromAsset);
                            PreviewLockScreen.this.tv_time.setTypeface(createFromAsset);
                        }
                    });
                    return;
                case 1:
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "font2.otf");
                            PreviewLockScreen.this.tv_date.setTypeface(createFromAsset);
                            PreviewLockScreen.this.tv_time.setTypeface(createFromAsset);
                        }
                    });
                    return;
                case 2:
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "font3.otf");
                            PreviewLockScreen.this.tv_date.setTypeface(createFromAsset);
                            PreviewLockScreen.this.tv_time.setTypeface(createFromAsset);
                        }
                    });
                    return;
                case 3:
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "font4.ttf");
                            PreviewLockScreen.this.tv_date.setTypeface(createFromAsset);
                            PreviewLockScreen.this.tv_time.setTypeface(createFromAsset);
                        }
                    });
                    return;
                case 4:
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Typeface createFromAsset = Typeface.createFromAsset(context.getAssets(), "font5.ttf");
                            PreviewLockScreen.this.tv_date.setTypeface(createFromAsset);
                            PreviewLockScreen.this.tv_time.setTypeface(createFromAsset);
                        }
                    });
                    return;
                default:
                    return;
            }
        }
    }

    private void showTimeOnLockScreen() {
        if (this.sharedPreferences.getBoolean("voice_lock_date_time", true)) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            long currentTimeMillis = System.currentTimeMillis();
                            Date date = new Date(currentTimeMillis);
                            Time time = new Time(currentTimeMillis);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE dd MMM, yyyy", Locale.ENGLISH);
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
                            String format = simpleDateFormat.format((java.util.Date) date);
                            PreviewLockScreen.this.tv_time.setText(simpleDateFormat2.format((java.util.Date) time));
                            PreviewLockScreen.this.tv_date.setText(format);
                        }
                    });
                }
            }, 0L, 1000L);
        }
    }

    public View getPinCodeView(final Context context) {
        final LinearLayout[] linearLayoutArr = new LinearLayout[10];
        final String[] strArr = {"ll_0", "ll_1", "ll_2", "ll_3", "ll_4", "ll_5", "ll_6", "ll_7", "ll_8", "ll_9"};
        View inflate = ((LayoutInflater) new ContextThemeWrapper(context, (int) R.style.AppTheme).getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lockscreen_pin_input_layout, (ViewGroup) null, false);
        final AppCompatEditText appCompatEditText = (AppCompatEditText) inflate.findViewById(R.id.et_password);
        final ImageView textView = (ImageView) inflate.findViewWithTag("show_hide_pass");
        final TextView textView2 = (TextView) inflate.findViewWithTag("tv_hint");
        final RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(R.id.root_layout_enter_pincode);
        Glide.with(context).load(Integer.valueOf((int) R.drawable.bg)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                relativeLayout.setBackground(drawable);
                return true;
            }
        }).submit();
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = (String) view.getTag();
                int i = 0;
                while (true) {
                    String[] strArr2 = strArr;
                    if (i < strArr2.length) {
                        if (str.equalsIgnoreCase(strArr2[i])) {
                            Log.e("LockScreenUI", i + " Button clicked");
                            if (appCompatEditText.getText() != null) {
                                appCompatEditText.setText(appCompatEditText.getText().toString() + i);
                                StringBuilder sb = new StringBuilder("edit text new value is ");
                                sb.append((Object) appCompatEditText.getText());
                                Log.e("LockScreenUI", sb.toString());
                                return;
                            }
                            return;
                        }
                        i++;
                    } else {
                        String obj = view.getTag().toString();
                        obj.hashCode();
                        char c = 65535;
                        switch (obj.hashCode()) {
                            case -1034734036:
                                if (obj.equals("show_hide_pass")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 169284344:
                                if (obj.equals("ll_clear_character")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 783438684:
                                if (obj.equals("ll_clear_all_text")) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                Log.e("LockScreenUI", String.valueOf(appCompatEditText.getInputType()));
                                if (appCompatEditText.getInputType() == 18) {
                                    appCompatEditText.setInputType(16);
                                    textView.setImageDrawable(getResources().getDrawable(R.drawable.ic_show));
                                    return;
                                }
                                appCompatEditText.setInputType(18);
                                textView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
                                return;
                            case 1:
                                if (appCompatEditText.getText() != null && appCompatEditText.getText().toString().length() > 0) {
                                    appCompatEditText.setText(appCompatEditText.getText().toString().substring(0, appCompatEditText.getText().toString().length() - 1));
                                    return;
                                }
                                Log.e("LockScreenUI", "Clear Last Character Clicked : Edit text is null or length is 0");
                                return;
                            case 2:
                                if (appCompatEditText.getText() != null && appCompatEditText.getText().toString().length() > 0) {
                                    appCompatEditText.setText("");
                                    return;
                                } else {
                                    Log.e("LockScreenUI", "Clear All Text Clicked : Edit text is null or length is 0");
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                }
            }
        };
        ((LinearLayout) inflate.findViewWithTag("ll_clear_all_text")).setOnClickListener(onClickListener);
        ((LinearLayout) inflate.findViewWithTag("ll_clear_character")).setOnClickListener(onClickListener);
        textView.setOnClickListener(onClickListener);
        for (int i = 0; i < 10; i++) {
            linearLayoutArr[i] = (LinearLayout) inflate.findViewWithTag(strArr[i]);
        }
        for (int i2 = 0; i2 < 10; i2++) {
            linearLayoutArr[i2].setOnClickListener(onClickListener);
        }
        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() >= 4) {
                    String string = context.getSharedPreferences("PASSWORDS", 0).getString("user_pin", "");
                    if ("5397".equalsIgnoreCase(editable.toString().trim().toLowerCase())) {
                        appCompatEditText.setText("");
                        VibrateAndVoice.passwordAccepted(context);
                        PreviewLockScreen.super.onBackPressed();
                        return;
                    } else if (string != null && string.trim().equalsIgnoreCase(editable.toString().trim())) {
                        appCompatEditText.setText("");
                        VibrateAndVoice.passwordAccepted(context);
                        PreviewLockScreen.super.onBackPressed();
                        return;
                    } else {
                        VibrateAndVoice.vibrate(context);
                        appCompatEditText.setText("");
                        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_anim);
                        appCompatEditText.setAnimation(loadAnimation);
                        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationStart(Animation animation) {
                                for (int i3 = 0; i3 < strArr.length; i3++) {
                                    linearLayoutArr[i3].setOnClickListener(null);
                                }
                                textView2.setText("INVALID PIN");
                                VibrateAndVoice.invalidPassword(context);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                for (int i3 = 0; i3 < strArr.length; i3++) {
                                    linearLayoutArr[i3].setOnClickListener(onClickListener);
                                }
                                textView2.setText("");
                            }
                        });
                        Log.e("LockScreenUI", "User old PIN is Null or Old pin & PIN Not Matched !");
                        return;
                    }
                }
                Log.e("LockScreenUI", "Entered pass is " + ((Object) editable));
            }
        });
        return inflate;
    }

    public void cancelAnimation() {
        this.mic_anim.setProgress(0.0f);
        this.mic_anim.cancelAnimation();
    }

    public void playAnimation() {
        this.mic_anim.playAnimation();
    }


    public class MySpeechRecognizationListener implements RecognitionListener {
        Context context;

        public MySpeechRecognizationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.e("LockScreenUI", "onReadyForSpeech");
            PreviewLockScreen.this.playAnimation();
            PreviewLockScreen.this.text_hint.setText(R.string.ready_to_speak);
            if (PreviewLockScreen.mySpeechRecognizerSingleton != null) {
                PreviewLockScreen.mySpeechRecognizerSingleton.setListening(true);
                Log.e("LockScreenUI", "onResults() : islistening value is " + PreviewLockScreen.mySpeechRecognizerSingleton.isListening());
            }
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.e("LockScreenUI", "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float f) {
            Log.e("LockScreenUI", "onRmsChanged");
            PreviewLockScreen.this.text_hint.setText(R.string.listening);
        }

        @Override
        public void onBufferReceived(byte[] bArr) {
            Log.e("LockScreenUI", "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.e("LockScreenUI", "onEndOfSpeech");
            PreviewLockScreen.this.cancelAnimation();
            VibrateAndVoice.vibrate(this.context);
            PreviewLockScreen.this.text_hint.setText(R.string.success);
            if (PreviewLockScreen.mySpeechRecognizerSingleton != null) {
                PreviewLockScreen.mySpeechRecognizerSingleton.setListening(false);
                Log.e("LockScreenUI", "onResults() : islistening value is " + PreviewLockScreen.mySpeechRecognizerSingleton.isListening());
            }
        }

        @Override
        public void onError(int i) {
            Log.e("LockScreenUI", "onError");
            PreviewLockScreen.this.cancelAnimation();
            VibrateAndVoice.vibrate(this.context);
            PreviewLockScreen.this.text_hint.setText(R.string.no_input_speak_your_pass);
            if (PreviewLockScreen.mySpeechRecognizerSingleton != null) {
                PreviewLockScreen.mySpeechRecognizerSingleton.setListening(false);
                Log.e("LockScreenUI", "onResults() : islistening value is " + PreviewLockScreen.mySpeechRecognizerSingleton.isListening());
            }
        }

        @Override
        public void onResults(Bundle bundle) {
            String str;

            PreviewLockScreen.this.cancelAnimation();
            ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
            if (stringArrayList != null) {
                str = "";
                for (String it : stringArrayList) {
                    str = it + "\n";
                }
            } else {
                str = "";
            }
            Log.e("LockScreenUI", "onResults : " + str);
            PreviewLockScreen.this.text_hint.setText(str);
            Log.e("LockScreenUI", "onResults : LockScreenUI");
            String string = this.context.getSharedPreferences("PASSWORDS", 0).getString("user_voice_password", "");
            if (str.length() > 0 && str.trim().toLowerCase(Locale.ENGLISH).matches("unlock please")) {
                Log.e("LockScreenUI", "Default Voice Password Filled");
                VibrateAndVoice.passwordAccepted(this.context);
                PreviewLockScreen.super.onBackPressed();
            } else if (string != null && string.length() > 0) {
                if (string.trim().equalsIgnoreCase(str.trim())) {
                    Log.e("LockScreenUI", "Voice Password Matched");
                    VibrateAndVoice.passwordAccepted(this.context);
                    PreviewLockScreen.super.onBackPressed();
                    Log.e("LockScreenUI", "  onPasswordMatchedListener.onVoicePasswordMatched called");
                } else {
                    VibrateAndVoice.invalidPassword(this.context);
                    Log.e("LockScreenUI", "User Voice Password not Matched. Old Password is - " + string + " & Voice password is - " + str);
                }
            } else {
                Log.e("LockScreenUI", "user old password is null or length is 0");
            }
            Log.e("LockScreenUI", "onResults End");
            if (PreviewLockScreen.mySpeechRecognizerSingleton != null) {
                PreviewLockScreen.mySpeechRecognizerSingleton.setListening(false);
                Log.e("LockScreenUI", "onResults() : islistening value is " + PreviewLockScreen.mySpeechRecognizerSingleton.isListening());
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.e("LockScreenUI", "onPartialResults");
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            Log.e("LockScreenUI", "onEvent");
        }
    }

    @Override
    public void onBackPressed() {
        ViewGroup viewGroup = this.keypad_password;
        if (viewGroup != null && ViewCompat.isAttachedToWindow(viewGroup)) {
            this.root_layout_lock_screen.removeView(this.keypad_password);
            return;
        }
        stopVoiceRecognizing();
        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        SpeechRecognizer speechRecognizer2 = speechRecognizer;
        if (speechRecognizer2 != null) {
            try {
                speechRecognizer2.cancel();
                speechRecognizer.destroy();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.e("LockScreenUI", "Illegal argument exception on Preview activity destroy");
            }
        }
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2 = mySpeechRecognizerSingleton;
        if (mySpeechRecognizerSingleton2 != null) {
            mySpeechRecognizerSingleton2.setListening(false);
            Log.e("LockScreenUI", "onResults() : islistening value is " + mySpeechRecognizerSingleton.isListening());
        }
        super.onDestroy();
    }
}
