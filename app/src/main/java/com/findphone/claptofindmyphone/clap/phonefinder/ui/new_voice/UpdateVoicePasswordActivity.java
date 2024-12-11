package com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.MySpeechRecognizerSingleton;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.VibrateAndVoice;


import java.util.ArrayList;


public class UpdateVoicePasswordActivity extends AppCompatActivity {
    public static final String TAG = "Set V P Activity";
    private static MySpeechRecognizerSingleton mySpeechRecognizerSingleton;
    private static Intent recognizerIntent;
    private static SpeechRecognizer speechRecognizer;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_set_voice_password);



        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2 = MySpeechRecognizerSingleton.getInstance(getApplication());
        mySpeechRecognizerSingleton = mySpeechRecognizerSingleton2;
        speechRecognizer = mySpeechRecognizerSingleton2.getSpeechRecognizer(getApplication());
        recognizerIntent = mySpeechRecognizerSingleton.getRecognizerIntent(getApplication());
        getSupportFragmentManager().beginTransaction().add(R.id.root_layout_set_fresh_voice_password, ConfirmOldVoicePasswordFragment.getInstance(), "fragment_confirm_old_voice_password").commitAllowingStateLoss();
    }


    public static class ConfirmOldVoicePasswordFragment extends Fragment {
        private final String TAG = "F_GetVoicePassword";
        private AppCompatButton btn_confirm;
        private LottieAnimationView mic_anim;
        private TextView mic_text_hint;
        private LottieAnimationView music_waves;
        private TextView text_hint;

        private ImageView back_button;

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_confirm_old_voice_password, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            if (getActivity() != null) {
                initWidgets(view);
                this.back_button.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());

                this.mic_anim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (ConfirmOldVoicePasswordFragment.this.getActivity() == null || ConfirmOldVoicePasswordFragment.this.getActivity().getApplication() == null) {
                            return;
                        }
                        ConfirmOldVoicePasswordFragment confirmOldVoicePasswordFragment = ConfirmOldVoicePasswordFragment.this;
                        confirmOldVoicePasswordFragment.startVoiceRecognizing(confirmOldVoicePasswordFragment.getActivity().getApplication());
                    }
                });
            }
        }

        private void initWidgets(View view) {
            this.mic_anim = (LottieAnimationView) view.findViewById(R.id.id_lav_record_audio);
            this.back_button = (ImageView) view.findViewById(R.id.back_button);
            this.music_waves = (LottieAnimationView) view.findViewById(R.id.id_lav_music_waves);
            this.text_hint = (TextView) view.findViewById(R.id.id_text_hint);
            this.mic_text_hint = (TextView) view.findViewById(R.id.id_tv_mic_text_hint);
            this.btn_confirm = (AppCompatButton) view.findViewById(R.id.btn_confirm_password);
        }

        @Override
        public void onPause() {
            stopVoiceRecognizing();
            Log.e("F_GetVoicePassword", "onPause()");
            super.onPause();
        }

        @Override
        public void onResume() {
            if (getActivity() != null) {
                if (getActivity().getApplication() != null) {
                    startVoiceRecognizing(getActivity().getApplication());
                    Log.e("F_GetVoicePassword", "onResume() : speech Recognization started from onResume().");
                } else {
                    Log.e("F_GetVoicePassword", "getApplication() is null, can not start speech Recognization from onResume()");
                }
            } else {
                Log.e("F_GetVoicePassword", "getActivity() is null, can not start speech Recognization from onResume()");
            }
            super.onResume();
        }

        public void playAnimation() {
            this.mic_anim.playAnimation();
            this.music_waves.playAnimation();
        }

        public void cancelAnimation() {
            this.mic_anim.cancelAnimation();
            this.mic_anim.setProgress(0.0f);
            this.music_waves.cancelAnimation();
            this.music_waves.setProgress(0.5f);
        }


        public void startVoiceRecognizing(Application application) {
            if (UpdateVoicePasswordActivity.speechRecognizer != null && UpdateVoicePasswordActivity.recognizerIntent != null && UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                Log.e("MYTAG", "ErrorNo: startVoiceRecognizing:+0");
                if (!UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening()) {
                    Log.e("MYTAG", "ErrorNo: startVoiceRecognizing:+1");
                    try {
                        UpdateVoicePasswordActivity.speechRecognizer.startListening(UpdateVoicePasswordActivity.recognizerIntent);
                        UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                        Log.e("F_GetVoicePassword", "startVoiceRecognizing() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                        UpdateVoicePasswordActivity.speechRecognizer.setRecognitionListener(new MySpeechRecognizationListener(application));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MYTAG", "ErrorNo: startVoiceRecognizing 2:" +e);
                    }
                } else {
                    Log.e("F_GetVoicePassword", "startVoiceRecognizing() speech recognizer already running : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }
            Log.e("F_GetVoicePassword", "Speech Recognization startVoiceRecognizing Called !");
        }

        private void stopVoiceRecognizing() {
            if (UpdateVoicePasswordActivity.speechRecognizer != null) {
                UpdateVoicePasswordActivity.speechRecognizer.stopListening();
                Log.e("F_GetVoicePassword", "Speech Recognization stopVoiceRecognizing Called !");
                return;
            }
            Log.e("F_GetVoicePassword", "Speech Recognization is null, can not start stoplistening !");
        }


        public class MySpeechRecognizationListener implements RecognitionListener {
            Context context;

            public MySpeechRecognizationListener(Context context) {
                this.context = context;
            }

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.e("F_GetVoicePassword", "onReadyForSpeech");
                ConfirmOldVoicePasswordFragment.this.playAnimation();
                ConfirmOldVoicePasswordFragment.this.mic_text_hint.setText("");
                if (ConfirmOldVoicePasswordFragment.this.btn_confirm.getVisibility() == View.VISIBLE) {
                    ConfirmOldVoicePasswordFragment.this.btn_confirm.setVisibility(View.INVISIBLE);
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                    Log.e("F_GetVoicePassword", "onReadyForSpeech() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("F_GetVoicePassword", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float f) {
                Log.e("F_GetVoicePassword", "onRmsChanged");
                ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.listening);
            }

            @Override
            public void onBufferReceived(byte[] bArr) {
                Log.e("F_GetVoicePassword", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("F_GetVoicePassword", "onEndOfSpeech");
                ConfirmOldVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onEndOfSpeech() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onError(int i) {
                Log.e("F_GetVoicePassword", "onError called : Error code is " + i);
                ConfirmOldVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                ConfirmOldVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on);
                ConfirmOldVoicePasswordFragment.this.text_hint.setText("No Input... Please speak\nyour old Voice password to confirm!");
                switch (i) {
                    case 1:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK_TIMEOUT");
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.network_timeout_confirm_old_voice_pass);
                        break;
                    case 2:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK");
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.network_error_confirm_old_voice_pass);
                        break;
                    case 3:
                        Log.e("F_GetVoicePassword", "ERROR_AUDIO");
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.audio_error_confirm_old_voice_pass);
                        break;
                    case 4:
                        Log.e("F_GetVoicePassword", "ERROR_SERVER");
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.error_server_confirm_old_voice_pass);
                        break;
                    case 5:
                        Log.e("F_GetVoicePassword", "ERROR_CLIENT");
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.error_client_confirm_old_voice_pass);
                        break;
                    case 6:
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.no_input_speak_your_pass_confirm_old_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_SPEECH_TIMEOUT");
                        break;
                    case 7:
                        Log.e("F_GetVoicePassword", "ERROR_NO_MATCH");
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.error_no_match_confirm_old_voice_pass);
                        break;
                    case 8:
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.recognizer_busy_confirm_old_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_RECOGNIZER_BUSY");
                        break;
                    case 9:
                        ConfirmOldVoicePasswordFragment.this.text_hint.setText(R.string.insufficeiant_permission_confirm_old_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onError() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                final String str;
                String str1;

                Log.e("F_GetVoicePassword", "onResults : SetVoicePasswordFragment");
                ConfirmOldVoicePasswordFragment.this.cancelAnimation();
                ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                if (stringArrayList != null) {
                    str1 = "";


                    for (String it : stringArrayList) {
                        str1 = it + "\n";
                    }
                } else {
                    str1 = "";
                }
                str = str1;
                Log.e("F_GetVoicePassword", "onResults : " + str);
                String string = this.context.getSharedPreferences("PASSWORDS", 0).getString("user_voice_password", "");
                if (string != null && string.trim().equalsIgnoreCase(str.trim())) {
                    new Handler(Looper.getMainLooper()).postDelayed(new AnonymousClass1(str), 50L);
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (str.length() > 2) {
                                TextView textView = ConfirmOldVoicePasswordFragment.this.text_hint;
                                textView.setText(str.trim().toUpperCase() + "\nYour old password not matched to this password. \nIn order to change your voice password, you must speak correct old password.");
                            }
                            ConfirmOldVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on);
                        }
                    }, 50L);
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onResults() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }


            class AnonymousClass1 implements Runnable {
                final String val$finalText;

                AnonymousClass1(String str) {
                    this.val$finalText = str;
                }

                @Override
                public void run() {
                    if (this.val$finalText.length() > 2) {
                        TextView textView = ConfirmOldVoicePasswordFragment.this.text_hint;
                        textView.setText(this.val$finalText.trim().toUpperCase() + "\nYour old Password confirmed.\nClick on Next Button to set new Voice Password.");
                    }
                    ConfirmOldVoicePasswordFragment.this.mic_text_hint.setText("Old Password Confirmed!");
                    if (ConfirmOldVoicePasswordFragment.this.btn_confirm.getVisibility() == View.INVISIBLE) {
                        ConfirmOldVoicePasswordFragment.this.btn_confirm.setVisibility(View.VISIBLE);
                        ConfirmOldVoicePasswordFragment.this.btn_confirm.setText(R.string.next);
                    }
                    final String str = this.val$finalText;
                    ConfirmOldVoicePasswordFragment.this.btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MySpeechRecognizationListener.this.context.getSharedPreferences("PASSWORDS", 0).edit().putString("temp_voice_pass", str).apply();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ConfirmOldVoicePasswordFragment.this.getActivity() != null) {
                                        ConfirmOldVoicePasswordFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root_layout_set_fresh_voice_password, SetVoicePasswordFragment.getInstance(), "fragment_set_voice_password").commitAllowingStateLoss();
                                    }
                                    Log.e("F_GetVoicePassword", "Set Current fragment to Confirm voice password");
                                }
                            }, 50L);
                        }
                    });
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e("F_GetVoicePassword", "onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("F_GetVoicePassword", "onEvent");
            }
        }

        public static ConfirmOldVoicePasswordFragment getInstance() {
            return new ConfirmOldVoicePasswordFragment();
        }
    }


    public static class SetVoicePasswordFragment extends Fragment {
        private final String TAG = "F_GetVoicePassword";
        private AppCompatButton btn_confirm;
        private LottieAnimationView mic_anim;
        private TextView mic_text_hint;
        private LottieAnimationView music_waves;
        private TextView text_hint;

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_set_voice_password, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            if (getActivity() != null) {
                initWidgets(view);
                this.mic_anim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (SetVoicePasswordFragment.this.getActivity() == null || SetVoicePasswordFragment.this.getActivity().getApplication() == null) {
                            return;
                        }
                        SetVoicePasswordFragment setVoicePasswordFragment = SetVoicePasswordFragment.this;
                        setVoicePasswordFragment.startVoiceRecognizing(setVoicePasswordFragment.getActivity().getApplication());
                    }
                });
            }
        }

        private void initWidgets(View view) {
            this.mic_anim = (LottieAnimationView) view.findViewById(R.id.id_lav_record_audio);
            this.music_waves = (LottieAnimationView) view.findViewById(R.id.id_lav_music_waves);
            this.text_hint = (TextView) view.findViewById(R.id.id_text_hint);
            this.mic_text_hint = (TextView) view.findViewById(R.id.id_tv_mic_text_hint);
            this.btn_confirm = (AppCompatButton) view.findViewById(R.id.btn_confirm_password);
        }

        @Override
        public void onPause() {
            stopVoiceRecognizing();
            Log.e("F_GetVoicePassword", "onPause()");
            super.onPause();
        }

        @Override
        public void onResume() {
            if (getActivity() != null) {
                if (getActivity().getApplication() != null) {
                    startVoiceRecognizing(getActivity().getApplication());
                    Log.e("F_GetVoicePassword", "onResume() : speech Recognization started from onResume().");
                } else {
                    Log.e("F_GetVoicePassword", "getApplication() is null, can not start speech Recognization from onResume()");
                }
            } else {
                Log.e("F_GetVoicePassword", "getActivity() is null, can not start speech Recognization from onResume()");
            }
            super.onResume();
        }

        public void playAnimation() {
            this.mic_anim.playAnimation();
            this.music_waves.playAnimation();
        }

        public void cancelAnimation() {
            this.mic_anim.cancelAnimation();
            this.mic_anim.setProgress(0.0f);
            this.music_waves.cancelAnimation();
            this.music_waves.setProgress(0.5f);
        }


        public void startVoiceRecognizing(Application application) {
            if (UpdateVoicePasswordActivity.speechRecognizer != null && UpdateVoicePasswordActivity.recognizerIntent != null && UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                if (!UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening()) {
                    try {
                        UpdateVoicePasswordActivity.speechRecognizer.startListening(UpdateVoicePasswordActivity.recognizerIntent);
                        UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                        Log.e("F_GetVoicePassword", "startVoiceRecognizing() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                        UpdateVoicePasswordActivity.speechRecognizer.setRecognitionListener(new MySpeechRecognizationListener(application));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Log.e("F_GetVoicePassword", "Security Exception occured while start listening called from speechRecognizer object");
                    }
                } else {
                    Log.e("F_GetVoicePassword", "startVoiceRecognizing() speech recognizer already running : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }
            Log.e("F_GetVoicePassword", "Speech Recognization startVoiceRecognizing Called !");
        }

        private void stopVoiceRecognizing() {
            if (UpdateVoicePasswordActivity.speechRecognizer != null) {
                UpdateVoicePasswordActivity.speechRecognizer.stopListening();
                Log.e("F_GetVoicePassword", "Speech Recognization stopVoiceRecognizing Called !");
                return;
            }
            Log.e("F_GetVoicePassword", "Speech Recognization is null, can not start stoplistening !");
        }


        public class MySpeechRecognizationListener implements RecognitionListener {
            Context context;

            public MySpeechRecognizationListener(Context context) {
                this.context = context;
            }

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.e("F_GetVoicePassword", "onReadyForSpeech");
                SetVoicePasswordFragment.this.playAnimation();
                SetVoicePasswordFragment.this.mic_text_hint.setText("");
                if (SetVoicePasswordFragment.this.btn_confirm.getVisibility() == View.VISIBLE) {
                    SetVoicePasswordFragment.this.btn_confirm.setVisibility(View.INVISIBLE);
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                    Log.e("F_GetVoicePassword", "onReadyForSpeech() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("F_GetVoicePassword", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float f) {
                Log.e("F_GetVoicePassword", "onRmsChanged");
                SetVoicePasswordFragment.this.text_hint.setText(R.string.listening);
            }

            @Override
            public void onBufferReceived(byte[] bArr) {
                Log.e("F_GetVoicePassword", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("F_GetVoicePassword", "onEndOfSpeech");
                SetVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onEndOfSpeech() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onError(int i) {
                Log.e("F_GetVoicePassword", "onError called : Error code is " + i);
                SetVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                SetVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on);
                SetVoicePasswordFragment.this.text_hint.setText("No Input... Please Speak\nyour Password to Record");
                switch (i) {
                    case 1:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK_TIMEOUT");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.network_timeout_set_voice_pass);
                        break;
                    case 2:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.network_error_set_voice_pass);
                        break;
                    case 3:
                        Log.e("F_GetVoicePassword", "ERROR_AUDIO");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.audio_error_set_voice_pass);
                        break;
                    case 4:
                        Log.e("F_GetVoicePassword", "ERROR_SERVER");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.error_server_set_voice_pass);
                        break;
                    case 5:
                        Log.e("F_GetVoicePassword", "ERROR_CLIENT");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.error_client_set_voice_pass);
                        break;
                    case 6:
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.no_input_speak_your_pass_set_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_SPEECH_TIMEOUT");
                        break;
                    case 7:
                        Log.e("F_GetVoicePassword", "ERROR_NO_MATCH");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.error_no_match_set_voice_pass);
                        break;
                    case 8:
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.recognizer_busy_set_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_RECOGNIZER_BUSY");
                        break;
                    case 9:
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.insufficeiant_permission_set_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onError() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onResults(Bundle bundle) {

                Log.e("F_GetVoicePassword", "onResults : SetVoicePasswordFragment");
                SetVoicePasswordFragment.this.cancelAnimation();
                ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                String str = "";
                if (stringArrayList != null) {


                    for (String it : stringArrayList) {
                        str = it + "\n";
                    }
                }
                Log.e("F_GetVoicePassword", "onResults : " + str);
                SetVoicePasswordFragment.this.text_hint.setText("You said : " + str.trim().toUpperCase() + "\n Click on 'CONFIRM VOICE PASSWORD' button\nto confirm your new voice password");
                SetVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on);
                if (SetVoicePasswordFragment.this.btn_confirm.getVisibility() == View.INVISIBLE) {
                    SetVoicePasswordFragment.this.btn_confirm.setVisibility(View.VISIBLE);
                    SetVoicePasswordFragment.this.btn_confirm.setText("Confirm Voice Password");
                }
                String finalStr = str;
                SetVoicePasswordFragment.this.btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MySpeechRecognizationListener.this.context.getSharedPreferences("PASSWORDS", 0).edit().putString("temp_voice_pass", finalStr).apply();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (SetVoicePasswordFragment.this.getActivity() != null) {
                                    SetVoicePasswordFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root_layout_set_fresh_voice_password, ConfirmVoicePasswordFragment.getInstance(), "fragment_confirm_voice_password").commitAllowingStateLoss();
                                }
                                Log.e("F_GetVoicePassword", "Set Current fragment to Confirm voice password");
                            }
                        }, 50L);
                    }
                });
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onResults() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e("F_GetVoicePassword", "onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("F_GetVoicePassword", "onEvent");
            }
        }

        public static SetVoicePasswordFragment getInstance() {
            return new SetVoicePasswordFragment();
        }
    }


    public static class ConfirmVoicePasswordFragment extends Fragment {
        private final String TAG = "F_GetVoicePassword";
        private AppCompatButton btn_done;
        private LottieAnimationView mic_anim;
        private TextView mic_text_hint;
        private LottieAnimationView music_waves;
        private TextView text_hint;

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_confirm_voice_password, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            if (getActivity() != null) {
                initWidgets(view);
                this.mic_anim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        if (ConfirmVoicePasswordFragment.this.getActivity() == null || ConfirmVoicePasswordFragment.this.getActivity().getApplication() == null) {
                            return;
                        }
                        ConfirmVoicePasswordFragment confirmVoicePasswordFragment = ConfirmVoicePasswordFragment.this;
                        confirmVoicePasswordFragment.startVoiceRecognizing(confirmVoicePasswordFragment.getActivity().getApplication());
                    }
                });
            }
        }

        private void initWidgets(View view) {
            this.mic_anim = (LottieAnimationView) view.findViewById(R.id.id_lav_record_audio);
            this.music_waves = (LottieAnimationView) view.findViewById(R.id.id_lav_music_waves);
            this.text_hint = (TextView) view.findViewById(R.id.id_text_hint);
            this.mic_text_hint = (TextView) view.findViewById(R.id.id_tv_mic_text_hint);
            this.btn_done = (AppCompatButton) view.findViewById(R.id.btn_done);
        }

        @Override
        public void onPause() {
            stopVoiceRecognizing();
            Log.e("F_GetVoicePassword", "onPause()");
            super.onPause();
        }

        @Override
        public void onResume() {
            if (getActivity() != null) {
                if (getActivity().getApplication() != null) {
                    startVoiceRecognizing(getActivity().getApplication());
                    Log.e("F_GetVoicePassword", "onResume() : speech Recognization started from onResume().");
                } else {
                    Log.e("F_GetVoicePassword", "getApplication() is null, can not start speech Recognization from onResume()");
                }
            } else {
                Log.e("F_GetVoicePassword", "getActivity() is null, can not start speech Recognization from onResume()");
            }
            super.onResume();
        }

        public void playAnimation() {
            this.mic_anim.playAnimation();
            this.music_waves.playAnimation();
        }

        public void cancelAnimation() {
            this.mic_anim.cancelAnimation();
            this.mic_anim.setProgress(0.0f);
            this.music_waves.cancelAnimation();
            this.music_waves.setProgress(0.5f);
        }


        public void startVoiceRecognizing(Application application) {
            if (UpdateVoicePasswordActivity.speechRecognizer != null && UpdateVoicePasswordActivity.recognizerIntent != null && UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                if (!UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening()) {
                    try {
                        UpdateVoicePasswordActivity.speechRecognizer.startListening(UpdateVoicePasswordActivity.recognizerIntent);
                        UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                        Log.e("F_GetVoicePassword", "startVoiceRecognizing() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                        UpdateVoicePasswordActivity.speechRecognizer.setRecognitionListener(new MySpeechRecognizationListener(application));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Log.e("F_GetVoicePassword", "Security Exception occured while start listening called from speechRecognizer object");
                    }
                } else {
                    Log.e("F_GetVoicePassword", "startVoiceRecognizing() speech recognizer already running : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }
            Log.e("F_GetVoicePassword", "Speech Recognization startVoiceRecognizing Called !");
        }

        private void stopVoiceRecognizing() {
            if (UpdateVoicePasswordActivity.speechRecognizer != null) {
                UpdateVoicePasswordActivity.speechRecognizer.stopListening();
                Log.e("F_GetVoicePassword", "Speech Recognization stopVoiceRecognizing Called !");
                return;
            }
            Log.e("F_GetVoicePassword", "Speech Recognization is null, can not start stoplistening !");
        }


        public class MySpeechRecognizationListener implements RecognitionListener {
            Context context;

            public MySpeechRecognizationListener(Context context) {
                this.context = context;
            }

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.e("F_GetVoicePassword", "onReadyForSpeech");
                ConfirmVoicePasswordFragment.this.playAnimation();
                ConfirmVoicePasswordFragment.this.mic_text_hint.setText("");
                if (ConfirmVoicePasswordFragment.this.btn_done.getVisibility() == View.VISIBLE) {
                    ConfirmVoicePasswordFragment.this.btn_done.setVisibility(View.INVISIBLE);
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                    Log.e("F_GetVoicePassword", "onReadyForSpeech() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("F_GetVoicePassword", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float f) {
                Log.e("F_GetVoicePassword", "onRmsChanged");
                ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.listening);
            }

            @Override
            public void onBufferReceived(byte[] bArr) {
                Log.e("F_GetVoicePassword", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("F_GetVoicePassword", "onEndOfSpeech");
                ConfirmVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onEndOfSpeech() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onError(int i) {
                Log.e("F_GetVoicePassword", "onError called : Error code is " + i);
                ConfirmVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                ConfirmVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on);
                ConfirmVoicePasswordFragment.this.text_hint.setText("No Input... Please Speak\nyour Password to Record");
                switch (i) {
                    case 1:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK_TIMEOUT");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.network_timeout_confirm_voice_pass);
                        break;
                    case 2:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.network_error_confirm_voice_pass);
                        break;
                    case 3:
                        Log.e("F_GetVoicePassword", "ERROR_AUDIO");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.audio_error_confirm_voice_pass);
                        break;
                    case 4:
                        Log.e("F_GetVoicePassword", "ERROR_SERVER");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.error_server_confirm_voice_pass);
                        break;
                    case 5:
                        Log.e("F_GetVoicePassword", "ERROR_CLIENT");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.error_client_confirm_voice_pass);
                        break;
                    case 6:
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.no_input_speak_your_pass_confirm_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_SPEECH_TIMEOUT");
                        break;
                    case 7:
                        Log.e("F_GetVoicePassword", "ERROR_NO_MATCH");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.error_no_match_confirm_voice_pass);
                        break;
                    case 8:
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.recognizer_busy_confirm_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_RECOGNIZER_BUSY");
                        break;
                    case 9:
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.insufficeiant_permission_confirm_voice_pass);
                        Log.e("F_GetVoicePassword", "ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onError() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                final String str;
                String str1;

                Log.e("F_GetVoicePassword", "onResults : ConfirmVoicePasswordFragment");
                ConfirmVoicePasswordFragment.this.cancelAnimation();
                ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                if (stringArrayList != null) {
                    str1 = "";


                    for (String it : stringArrayList) {
                        str1 = it + "\n";
                    }
                } else {
                    str1 = "";
                }
                str = str1;
                Log.e("F_GetVoicePassword", "onResults : " + str);
                String string = this.context.getSharedPreferences("PASSWORDS", 0).getString("temp_voice_pass", "");
                if (string != null && string.length() > 0 && string.equalsIgnoreCase(str)) {
                    Log.e("F_GetVoicePassword", "Old Temp Pass is : " + string + " ---- New Temp Pass is : " + str);
                    ConfirmVoicePasswordFragment.this.text_hint.setText("Cheers! Your New Voice Password is '" + str.trim().toUpperCase() + "'");
                    ConfirmVoicePasswordFragment.this.mic_text_hint.setText("New Voice Password has been set successfully");
                    if (ConfirmVoicePasswordFragment.this.btn_done.getVisibility() == View.INVISIBLE) {
                        ConfirmVoicePasswordFragment.this.btn_done.setVisibility(View.VISIBLE);
                        ConfirmVoicePasswordFragment.this.btn_done.setText(R.string.done);
                    }
                    ConfirmVoicePasswordFragment.this.btn_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MySpeechRecognizationListener.this.context.getSharedPreferences("PASSWORDS", 0).edit().putString("user_voice_password", str).apply();
                            Intent intent = new Intent();
                            intent.putExtra("result", "result_ok");
                            if (ConfirmVoicePasswordFragment.this.getActivity() != null) {
                                ConfirmVoicePasswordFragment.this.getActivity().setResult(-1, intent);
                                Toast.makeText(ConfirmVoicePasswordFragment.this.getActivity(),"Voice password has been changed...",Toast.LENGTH_LONG).show();
                                ConfirmVoicePasswordFragment.this.getActivity().finish();
                            }
                        }
                    });
                } else {
                    ConfirmVoicePasswordFragment.this.text_hint.setText(str.trim().toUpperCase() + "\nThis Voice Password does not\nmatch to your previous Password.");
                    ConfirmVoicePasswordFragment.this.mic_text_hint.setText("Tap on Mic to Speak Again…\nor\nClick 'BACK' button to set new password again");
                    Log.e("F_GetVoicePassword", "Old pass & New Temp Pass does not match" + str);
                    if (ConfirmVoicePasswordFragment.this.btn_done.getVisibility() == View.INVISIBLE) {
                        ConfirmVoicePasswordFragment.this.btn_done.setVisibility(View.VISIBLE);
                        ConfirmVoicePasswordFragment.this.btn_done.setText(R.string.back);
                    }
                    ConfirmVoicePasswordFragment.this.btn_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ConfirmVoicePasswordFragment.this.getActivity() != null) {
                                        ConfirmVoicePasswordFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root_layout_set_fresh_voice_password, SetVoicePasswordFragment.getInstance(), "fragment_set_voice_password").commitAllowingStateLoss();
                                    }
                                }
                            }, 100L);
                        }
                    });
                }
                if (UpdateVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onResults() : islistening value is " + UpdateVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e("F_GetVoicePassword", "onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("F_GetVoicePassword", "onEvent");
            }
        }

        public static ConfirmVoicePasswordFragment getInstance() {
            return new ConfirmVoicePasswordFragment();
        }
    }


    @Override
    public void onPause() {
        stopVoiceRecognizing();
        super.onPause();
    }

    private void stopVoiceRecognizing() {
        SpeechRecognizer speechRecognizer2 = speechRecognizer;
        if (speechRecognizer2 != null) {
            speechRecognizer2.stopListening();
            Log.e(TAG, "Speech Recognization stopVoiceRecognizing Called !");
            return;
        }
        Log.e(TAG, "Speech Recognization is null, can not start stoplistening !");
    }


    @Override
    public void onDestroy() {
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2 = mySpeechRecognizerSingleton;
        if (mySpeechRecognizerSingleton2 != null) {
            mySpeechRecognizerSingleton2.setListening(false);
            Log.e(TAG, "onResults() : islistening value is " + mySpeechRecognizerSingleton.isListening());
        }
        SpeechRecognizer speechRecognizer2 = speechRecognizer;
        if (speechRecognizer2 != null) {
            try {
                speechRecognizer2.cancel();
                speechRecognizer.destroy();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.e(TAG, "Illegal argument exception on activity destroy");
            }
        }
        super.onDestroy();
    }
}
