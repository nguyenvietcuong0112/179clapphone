package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.util.Log;


public class MySpeechRecognizerSingleton {
    public static String TAG = "MyRecognizerSingleton";
    static MySpeechRecognizerSingleton mySpeechRecognizerSingleton;
    private boolean isListening;
    Intent recognizerIntent;
    SpeechRecognizer speechRecognizer;

    private MySpeechRecognizerSingleton() {
        if (this.speechRecognizer != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static MySpeechRecognizerSingleton getInstance(Application application) {
        MySpeechRecognizerSingleton mySpeechRecognizerSingleton2 = mySpeechRecognizerSingleton;
        if (mySpeechRecognizerSingleton2 == null) {
            MySpeechRecognizerSingleton mySpeechRecognizerSingleton3 = new MySpeechRecognizerSingleton();
            mySpeechRecognizerSingleton = mySpeechRecognizerSingleton3;
            return mySpeechRecognizerSingleton3;
        }
        return mySpeechRecognizerSingleton2;
    }

    public SpeechRecognizer getSpeechRecognizer(Application application) {
        if (this.speechRecognizer == null) {
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(application.getApplicationContext(), new ComponentName(application.getPackageName(), String.valueOf(getClass())));
            String str = TAG;
            Log.e(str, "Speech Recognizer Initialized again & hash code is - " + this.speechRecognizer.hashCode());
            return this.speechRecognizer;
        }
        String str2 = TAG;
        Log.e(str2, "Speech Recognizer hash code is - " + this.recognizerIntent.hashCode());
        return this.speechRecognizer;
    }

    public Intent getRecognizerIntent(Application application) {
        if (this.recognizerIntent == null) {
            Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            this.recognizerIntent = intent;
            intent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", "en");
            this.recognizerIntent.putExtra("calling_package", application.getApplicationContext().getPackageName());
            this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
            this.recognizerIntent.putExtra("android.speech.extra.MAX_RESULTS", 1);
            String str = TAG;
            Log.e(str, "Recognizer Intent Initialized again & hash code is - " + this.recognizerIntent.hashCode());
            return this.recognizerIntent;
        }
        String str2 = TAG;
        Log.e(str2, "Recognizer Intent hash code is - " + this.recognizerIntent.hashCode());
        return this.recognizerIntent;
    }

    public void initSpeechRecognizer(Application application) {
        if (this.speechRecognizer == null) {
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(application.getApplicationContext());
            String str = TAG;
            Log.e(str, "Speech Recognizer initialized & hash code is - " + this.speechRecognizer.hashCode());
        }
    }

    public void initRecognizerIntent(Application application) {
        if (this.recognizerIntent == null) {
            Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
            this.recognizerIntent = intent;
            intent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", "en");
            this.recognizerIntent.putExtra("calling_package", application.getApplicationContext().getPackageName());
            this.recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
            this.recognizerIntent.putExtra("android.speech.extra.MAX_RESULTS", 1);
            String str = TAG;
            Log.e(str, "Recognizer Intent initialized & hash code is - " + this.recognizerIntent.hashCode());
        }
    }

    public boolean isListening() {
        return this.isListening;
    }

    public void setListening(boolean z) {
        this.isListening = z;
    }
}
