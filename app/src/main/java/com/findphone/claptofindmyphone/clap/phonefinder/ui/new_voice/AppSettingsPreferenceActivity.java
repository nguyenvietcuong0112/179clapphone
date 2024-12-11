package com.findphone.claptofindmyphone.clap.phonefinder.ui.new_voice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.StartStopLockService;


public class AppSettingsPreferenceActivity extends AppCompatActivity {
    public static final String TAG = "AppSettings P A";

    private ImageView back_button;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_settings);

        this.back_button = (ImageView) findViewById(R.id.back_button);
        this.back_button.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.preference_root, new Setting_Preference_Fragment(), "setting_preference_fragment");
        beginTransaction.commit();
    }


    public static class Setting_Preference_Fragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        CheckBoxPreference cbp_datetime;
        CheckBoxPreference cbp_lockEnableDisable;
        CheckBoxPreference cbp_sound;
        CheckBoxPreference cbp_vibration;


        PreferenceManager preferenceManager;
        Preference preview;
        SharedPreferences sp;
        StartStopLockService startStopLockService = new StartStopLockService();

        @Override
        public void onCreatePreferences(Bundle bundle, String str) {
            setPreferencesFromResource(R.xml.preference_screen, str);
            if (getActivity() != null) {
                PreferenceManager preferenceManager = getPreferenceManager();
                this.preferenceManager = preferenceManager;
                preferenceManager.setSharedPreferencesName("voice_recognition_preference");
                this.sp = getActivity().getSharedPreferences("voice_recognition_preference", 0);


                this.cbp_lockEnableDisable = (CheckBoxPreference) findPreference("lock_service");
                this.cbp_datetime = (CheckBoxPreference) findPreference("voice_lock_date_time");
                this.cbp_sound = (CheckBoxPreference) findPreference("sound_flag");
                this.cbp_vibration = (CheckBoxPreference) findPreference("vibration_flag");
                Preference findPreference = findPreference("preview");
                this.preview = findPreference;
                if (findPreference != null) {
                    findPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Setting_Preference_Fragment.this.startActivity(new Intent(Setting_Preference_Fragment.this.getActivity(), PreviewLockScreen.class));
                            return true;
                        }
                    });
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();


            CheckBoxPreference checkBoxPreference = this.cbp_lockEnableDisable;
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(this.sp.getBoolean("lock_service", true));
                this.cbp_lockEnableDisable.setOnPreferenceChangeListener(this);
            }
            CheckBoxPreference checkBoxPreference2 = this.cbp_datetime;
            if (checkBoxPreference2 != null) {
                checkBoxPreference2.setChecked(this.sp.getBoolean("voice_lock_date_time", true));
                this.cbp_datetime.setOnPreferenceChangeListener(this);
            }
            CheckBoxPreference checkBoxPreference3 = this.cbp_sound;
            if (checkBoxPreference3 != null) {
                checkBoxPreference3.setChecked(this.sp.getBoolean("sound_flag", true));
                this.cbp_sound.setOnPreferenceChangeListener(this);
            }
            CheckBoxPreference checkBoxPreference4 = this.cbp_vibration;
            if (checkBoxPreference4 != null) {
                checkBoxPreference4.setChecked(this.sp.getBoolean("vibration_flag", true));
                this.cbp_vibration.setOnPreferenceChangeListener(this);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object obj) {
            String key = preference.getKey();


            if (key.equalsIgnoreCase("lock_service")) {
                this.sp.edit().putBoolean("lock_service", this.cbp_datetime.isChecked()).apply();
                if (!this.cbp_lockEnableDisable.isChecked()) {
                    this.startStopLockService.startServiceForAllAndroidAPILevel(getActivity());
                    Log.e(AppSettingsPreferenceActivity.TAG, "Lock Service Started");
                    return true;
                }
                this.startStopLockService.stopServiceInAllAPILevel(getActivity());
                Log.e(AppSettingsPreferenceActivity.TAG, "Lock Service Stopped");
                return true;
            } else if (key.equalsIgnoreCase("voice_lock_date_time")) {
                this.sp.edit().putBoolean("voice_lock_date_time", this.cbp_datetime.isChecked()).apply();
                Log.e(AppSettingsPreferenceActivity.TAG, "Date Time Shwo hide " + this.cbp_datetime.isChecked());
                return true;
            } else if (key.equalsIgnoreCase("sound_flag")) {
                this.sp.edit().putBoolean("sound_flag", this.cbp_sound.isChecked()).apply();
                Log.e(AppSettingsPreferenceActivity.TAG, "Sound enabled / disabled " + this.cbp_sound.isChecked());
                return true;
            } else if (key.equalsIgnoreCase("vibration_flag")) {
                this.sp.edit().putBoolean("vibration_flag", this.cbp_vibration.isChecked()).apply();
                Log.e(AppSettingsPreferenceActivity.TAG, "vibration enabled / disabled " + this.cbp_vibration.isChecked());
                return true;
            } else {
                return true;
            }
        }
    }
}
