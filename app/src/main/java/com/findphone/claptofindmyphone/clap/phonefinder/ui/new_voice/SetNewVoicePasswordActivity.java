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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.MySpeechRecognizerSingleton;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.VibrateAndVoice;


import java.util.ArrayList;


public class SetNewVoicePasswordActivity extends AppCompatActivity {
    private static final String TAG = "S N VoicePassActivity";
    static MySpeechRecognizerSingleton mySpeechRecognizerSingleton;
    static Intent recognizerIntent;
    static SpeechRecognizer speechRecognizer;


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
        getSupportFragmentManager().beginTransaction().add(R.id.root_layout_set_fresh_voice_password, SetVoicePasswordFragment.getInstance(), "fragment_set_voice_password").commitAllowingStateLoss();
    }


    public static class SetVoicePasswordFragment extends Fragment {
        private final String TAG = "F_GetVoicePassword";
        private AppCompatButton btn_confirm;
        private LottieAnimationView mic_anim;
        private TextView mic_text_hint;
        private LottieAnimationView music_waves;
        private TextView text_hint;

        private ImageView back_button;

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_set_voice_password, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            if (getActivity() != null) {
                initWidgets(view);
                Log.e("F_GetVoicePassword", "onViewCreated");
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
                this.back_button.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());

            }
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

        public void cancelAnimation() {
            this.mic_anim.cancelAnimation();
            this.mic_anim.setProgress(0.0f);
            this.music_waves.cancelAnimation();
            this.music_waves.setProgress(0.5f);
        }

        public void playAnimation() {
            this.mic_anim.playAnimation();
            this.music_waves.playAnimation();
        }

        private void initWidgets(View view) {
            this.mic_anim = (LottieAnimationView) view.findViewById(R.id.id_lav_record_audio);
            this.music_waves = (LottieAnimationView) view.findViewById(R.id.id_lav_music_waves);
            this.text_hint = (TextView) view.findViewById(R.id.id_text_hint);
            this.mic_text_hint = (TextView) view.findViewById(R.id.id_tv_mic_text_hint);
            this.btn_confirm = (AppCompatButton) view.findViewById(R.id.btn_confirm_password);
            this.back_button = (ImageView) view.findViewById(R.id.back_button);
        }


        public void startVoiceRecognizing(Application application) {
            if (SetNewVoicePasswordActivity.speechRecognizer != null && SetNewVoicePasswordActivity.recognizerIntent != null && SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                if (!SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening()) {
                    try {
                        SetNewVoicePasswordActivity.speechRecognizer.startListening(SetNewVoicePasswordActivity.recognizerIntent);
                        SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                        Log.e("F_GetVoicePassword", "startVoiceRecognizing() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());

                        SetNewVoicePasswordActivity.speechRecognizer.setRecognitionListener(new MySpeechRecognizationListener(application));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MYTAG", "ErrorNo: startVoiceRecognizing:" +e);

                        Log.e("F_GetVoicePassword", "Security Exception occured while start listening called from speechRecognizer object");
                    }
                } else {
                    Log.e("F_GetVoicePassword", "startVoiceRecognizing() speech recognizer already running : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            } else {
                if (SetNewVoicePasswordActivity.speechRecognizer == null) {
                    Log.e("F_GetVoicePassword", "speech recogniser null");
                }
                if (SetNewVoicePasswordActivity.recognizerIntent == null) {
                    Log.e("F_GetVoicePassword", "recogniser Internt null");
                }
            }
            Log.e("F_GetVoicePassword", "Speech Recognization startVoiceRecognizing Called !");
        }

        private void stopVoiceRecognizing() {
            if (SetNewVoicePasswordActivity.speechRecognizer != null) {
                SetNewVoicePasswordActivity.speechRecognizer.stopListening();
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
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                    Log.e("F_GetVoicePassword", "onReadyForSpeech() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
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
                SetVoicePasswordFragment.this.text_hint.setText("");
                SetVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on_mic);
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onEndOfSpeech() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onError(int i) {
                Log.e("F_GetVoicePassword", "onError");
                SetVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                SetVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on_mic);
                switch (i) { 
                    case 1:
                        Log.e("F_GetVoicePassword", "ERROR_NETWORK_TIMEOUT");
                        SetVoicePasswordFragment.this.text_hint.setText(R.string.network_error_set_voice_pass);
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
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onError() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
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
                String finalStr = str;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (finalStr.length() > 2) {
                            TextView textView = SetVoicePasswordFragment.this.text_hint;
                            textView.setText(finalStr.trim().toUpperCase() + "\nClick on \"CONFIRM VOICE PASSWORD\" Button to Confirm this Voice Password.");
                            if (SetVoicePasswordFragment.this.btn_confirm.getVisibility() == View.INVISIBLE) {
                                SetVoicePasswordFragment.this.btn_confirm.setVisibility(View.VISIBLE);
                                SetVoicePasswordFragment.this.btn_confirm.setText("Confirm Voice Password");
                            }
                        }
                    }
                }, 50L);
                String finalStr1 = str;
                SetVoicePasswordFragment.this.btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MySpeechRecognizationListener.this.context.getSharedPreferences("PASSWORDS", 0).edit().putString("temp_voice_pass", finalStr1).apply();
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
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_GetVoicePassword", "onResults() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
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
        private final String TAG = "F_ConfirmVoicePassword";
        private AppCompatButton btn_done;
        private LottieAnimationView mic_anim;
        private TextView mic_text_hint;
        private LottieAnimationView music_waves;
        private TextView text_hint;

        private ImageView back_button;

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.fragment_confirm_voice_password, viewGroup, false);
        }

        @Override
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            if (getActivity() != null) {
                initWidgets(view);
                Log.e("F_ConfirmVoicePassword", "onViewCreated");
                this.back_button.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());

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

        @Override
        public void onPause() {
            stopVoiceRecognizing();
            Log.e("F_ConfirmVoicePassword", "onPause()");
            super.onPause();
        }

        @Override
        public void onResume() {
            if (getActivity() != null) {
                if (getActivity().getApplication() != null) {
                    startVoiceRecognizing(getActivity().getApplication());
                    Log.e("F_ConfirmVoicePassword", "onResume() : speech Recognization started from onResume().");
                } else {
                    Log.e("F_ConfirmVoicePassword", "getApplication() is null, can not start speech Recognization from onResume()");
                }
            } else {
                Log.e("F_ConfirmVoicePassword", "getActivity() is null, can not start speech Recognization from onResume()");
            }
            super.onResume();
        }

        private void initWidgets(View view) {
            this.mic_anim = (LottieAnimationView) view.findViewById(R.id.id_lav_record_audio);
            this.back_button = (ImageView) view.findViewById(R.id.back_button);
            this.music_waves = (LottieAnimationView) view.findViewById(R.id.id_lav_music_waves);
            this.text_hint = (TextView) view.findViewById(R.id.id_text_hint);
            this.mic_text_hint = (TextView) view.findViewById(R.id.id_tv_mic_text_hint);
            this.btn_done = (AppCompatButton) view.findViewById(R.id.btn_done);
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
            if (SetNewVoicePasswordActivity.speechRecognizer != null && SetNewVoicePasswordActivity.recognizerIntent != null && SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                if (!SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening()) {
                    try {
                        SetNewVoicePasswordActivity.speechRecognizer.startListening(SetNewVoicePasswordActivity.recognizerIntent);
                        SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                        Log.e("F_ConfirmVoicePassword", "startVoiceRecognizing() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                        SetNewVoicePasswordActivity.speechRecognizer.setRecognitionListener(new MySpeechRecognizationListener(application));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        Log.e("F_ConfirmVoicePassword", "Security Exception occured while start listening called from speechRecognizer object");
                    }
                } else {
                    Log.e("F_ConfirmVoicePassword", "startVoiceRecognizing() speech recognizer already running : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }
            Log.e("F_ConfirmVoicePassword", "Speech Recognization startVoiceRecognizing Called !");
        }

        private void stopVoiceRecognizing() {
            if (SetNewVoicePasswordActivity.speechRecognizer != null) {
                SetNewVoicePasswordActivity.speechRecognizer.stopListening();
                Log.e("F_ConfirmVoicePassword", "Speech Recognization stopVoiceRecognizing Called !");
                return;
            }
            Log.e("F_ConfirmVoicePassword", "Speech Recognization is null, can not start stoplistening !");
        }


        public class MySpeechRecognizationListener implements RecognitionListener {
            Context context;

            public MySpeechRecognizationListener(Context context) {
                this.context = context;
            }

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.e("F_ConfirmVoicePassword", "onReadyForSpeech");
                ConfirmVoicePasswordFragment.this.playAnimation();
                ConfirmVoicePasswordFragment.this.mic_text_hint.setText("");
                if (ConfirmVoicePasswordFragment.this.btn_done.getVisibility() == View.VISIBLE) {
                    ConfirmVoicePasswordFragment.this.btn_done.setVisibility(View.INVISIBLE);
                }
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(true);
                    Log.e("F_ConfirmVoicePassword", "onReadyForSpeech() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("F_ConfirmVoicePassword", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float f) {
                Log.e("F_ConfirmVoicePassword", "onRmsChanged");
                ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.listening);
            }

            @Override
            public void onBufferReceived(byte[] bArr) {
                Log.e("F_ConfirmVoicePassword", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("F_ConfirmVoicePassword", "onEndOfSpeech");
                ConfirmVoicePasswordFragment.this.cancelAnimation();
                VibrateAndVoice.vibrate(this.context);
                ConfirmVoicePasswordFragment.this.text_hint.setText("");
                ConfirmVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on_mic);
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_ConfirmVoicePassword", "onEndOfSpeech() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onError(int i) {
                Log.e("F_ConfirmVoicePassword", "onError");
                ConfirmVoicePasswordFragment.this.cancelAnimation();
                ConfirmVoicePasswordFragment.this.mic_text_hint.setText(R.string.tap_on_mic);
                VibrateAndVoice.vibrate(this.context);
                switch (i) {
                    case 1:
                        Log.e("F_ConfirmVoicePassword", "ERROR_NETWORK_TIMEOUT");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.network_timeout_confirm_voice_pass);
                        break;
                    case 2:
                        Log.e("F_ConfirmVoicePassword", "ERROR_NETWORK");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.network_error_confirm_voice_pass);
                        break;
                    case 3:
                        Log.e("F_ConfirmVoicePassword", "ERROR_AUDIO");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.audio_error_confirm_voice_pass);
                        break;
                    case 4:
                        Log.e("F_ConfirmVoicePassword", "ERROR_SERVER");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.error_server_confirm_voice_pass);
                        break;
                    case 5:
                        Log.e("F_ConfirmVoicePassword", "ERROR_CLIENT");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.error_client_confirm_voice_pass);
                        break;
                    case 6:
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.no_input_speak_your_pass_confirm_voice_pass);
                        Log.e("F_ConfirmVoicePassword", "ERROR_SPEECH_TIMEOUT");
                        break;
                    case 7:
                        Log.e("F_ConfirmVoicePassword", "ERROR_NO_MATCH");
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.error_no_match_confirm_voice_pass);
                        break;
                    case 8:
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.recognizer_busy_confirm_voice_pass);
                        Log.e("F_ConfirmVoicePassword", "ERROR_RECOGNIZER_BUSY");
                        break;
                    case 9:
                        ConfirmVoicePasswordFragment.this.text_hint.setText(R.string.insufficeiant_permission_confirm_voice_pass);
                        Log.e("F_ConfirmVoicePassword", "ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                }
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_ConfirmVoicePassword", "onError() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                final String str;
                String str1;

                Log.e("F_ConfirmVoicePassword", "onResults : ConfirmVoicePasswordFragment");
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
                Log.e("F_ConfirmVoicePassword", "onResults : " + str);
                final String string = this.context.getSharedPreferences("PASSWORDS", 0).getString("temp_voice_pass", "");
                if (string != null && string.length() > 0 && string.equalsIgnoreCase(str)) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (str.length() > 2) {
                                TextView textView = ConfirmVoicePasswordFragment.this.text_hint;
                                textView.setText(str.trim().toUpperCase() + "\nVoice Password Confirmed. Click on \"DONE\" button to Save this voice password.");
                            }
                            Log.e("F_ConfirmVoicePassword", "Old Temp Pass is : " + string + " ---- New Temp Pass is : " + str);
                            if (ConfirmVoicePasswordFragment.this.btn_done.getVisibility() == View.INVISIBLE) {
                                ConfirmVoicePasswordFragment.this.btn_done.setVisibility(View.VISIBLE);
                                ConfirmVoicePasswordFragment.this.btn_done.setText("Done");
                            }
                            ConfirmVoicePasswordFragment.this.btn_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MySpeechRecognizationListener.this.context.getSharedPreferences("PASSWORDS", 0).edit().putString("user_voice_password", str).apply();
                                    Intent intent = new Intent();
                                    intent.putExtra("result", "result_ok");
                                    if (ConfirmVoicePasswordFragment.this.getActivity() != null) {
                                        ConfirmVoicePasswordFragment.this.getActivity().setResult(-1, intent);
                                        ConfirmVoicePasswordFragment.this.getActivity().finish();
                                    }
                                }
                            });
                        }
                    }, 50L);
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new AnonymousClass2(str), 50L);
                }
                if (SetNewVoicePasswordActivity.mySpeechRecognizerSingleton != null) {
                    SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.setListening(false);
                    Log.e("F_ConfirmVoicePassword", "onResults() : islistening value is " + SetNewVoicePasswordActivity.mySpeechRecognizerSingleton.isListening());
                }
            }


            class AnonymousClass2 implements Runnable {
                final String val$finalText1;

                AnonymousClass2(String str) {
                    this.val$finalText1 = str;
                }

                @Override
                public void run() {
                    if (this.val$finalText1.length() > 2) {
                        TextView textView = ConfirmVoicePasswordFragment.this.text_hint;
                        textView.setText(this.val$finalText1.trim().toUpperCase() + "\nThis Voice Password does not match to your previous Password.");
                    }
                    Log.e("F_ConfirmVoicePassword", "Old pass & New Temp Pass does not match" + this.val$finalText1);
                    if (ConfirmVoicePasswordFragment.this.btn_done.getVisibility() == View.INVISIBLE) {
                        ConfirmVoicePasswordFragment.this.btn_done.setVisibility(View.VISIBLE);
                        ConfirmVoicePasswordFragment.this.btn_done.setText("Back");
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
                                    Log.e("F_ConfirmVoicePassword", "Back button clicked");
                                }
                            }, 50L);
                        }
                    });
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e("F_ConfirmVoicePassword", "onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("F_ConfirmVoicePassword", "onEvent");
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
        SpeechRecognizer speechRecognizer2 = speechRecognizer;
        if (speechRecognizer2 != null) {
            try {
                speechRecognizer2.cancel();
                speechRecognizer.destroy();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.e(TAG, "Illegal Argument exception while destroying speech recognizer");
            }
        }
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2 = mySpeechRecognizerSingleton;
        if (mySpeechRecognizerSingleton2 != null) {
            mySpeechRecognizerSingleton2.setListening(false);
            Log.e(UpdateVoicePasswordActivity.TAG, "onResults() : islistening value is " + mySpeechRecognizerSingleton.isListening());
        }
        super.onDestroy();
    }
}
