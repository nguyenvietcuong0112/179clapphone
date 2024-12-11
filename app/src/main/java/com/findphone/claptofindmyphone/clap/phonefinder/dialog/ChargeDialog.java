package com.findphone.claptofindmyphone.clap.phonefinder.dialog;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.databinding.ActivityChargeDialogBinding;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mallegan.ads.callback.NativeCallback;
import com.mallegan.ads.util.Admob;

import io.paperdb.Paper;

import java.util.ArrayList;
import java.util.List;


public class ChargeDialog extends AppCompatActivity {

    ArrayList<String> app_data;
    LocalBroadcastManager mLocalBroadcastManager;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.durga.action.close")) {
                ChargeDialog.this.finish();
            }
        }
    };
    int counter = 0;
    Handler memoryHandler = new Handler();

    ActivityChargeDialogBinding binding;

    @Override

    public void onDestroy() {
        super.onDestroy();
        this.mLocalBroadcastManager.unregisterReceiver(this.mBroadcastReceiver);
    }

    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        supportRequestWindowFeature(1);
        binding = ActivityChargeDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFinishOnTouchOutside(true);
        if (Build.VERSION.SDK_INT != 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.durga.action.close");
        this.mLocalBroadcastManager.registerReceiver(this.mBroadcastReceiver, intentFilter);


        String str = null;
        if (((Boolean) Paper.book().read("for_boost", false)).booleanValue()) {
            binding.forCharger.setVisibility(View.GONE);
            binding.forBoost.setVisibility(View.VISIBLE);
            binding.layout.setBackground(getResources().getDrawable(R.drawable.charger_bg));
            new getAppData().execute(new String[0]);
            return;
        }
        binding.forCharger.setVisibility(View.VISIBLE);
        binding.forBoost.setVisibility(View.GONE);
        binding.layout.setBackground(getResources().getDrawable(R.drawable.charger_bg));
        if (bundle == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                str = extras.getString(NotificationCompat.CATEGORY_STATUS);
            }
        } else {
            str = (String) bundle.getSerializable(NotificationCompat.CATEGORY_STATUS);
        }
        if (str == null) {
            str = "connected";
        }
        if (str.equalsIgnoreCase("connected")) {
            binding.firstText.setText("Voltage");
            binding.voltage.setText((CharSequence) Paper.book().read("Voltage", "0 mV"));
            binding.icon1.setImageResource(R.drawable.icon_1_con);
            binding.secondText.setText("Health");
            binding.health.setText((CharSequence) Paper.book().read("Health", "Good"));
            binding.icon2.setImageResource(R.drawable.icon_2_con);
//            binding.batteryMeterView.setChargeLevel((Integer) Paper.book().read("Battery_pct_start", 0));
            TextView textView = binding.charingLevel;
            textView.setText(Paper.book().read("Battery_pct_start", 0) + "%");
            binding.charingLevel.setText("Charging");
        } else {
            int batteryPercent = getBatteryPercent();
            binding.firstText.setText("Charge Duration");
            TextView textView2 = binding.voltage;
            textView2.setText(((String) Paper.book().read("start_time", "00:00")) + " - " + ((String) Paper.book().read("end_time", "00:00")));
            binding.icon1.setImageResource(R.drawable.icon_1_dis);
            binding.secondText.setText("Charge Quantity");
            TextView textView3 = binding.health;
            textView3.setText(Paper.book().read("Battery_pct_start", 0) + "% - " + batteryPercent + "%");
            binding.icon2.setImageResource(R.drawable.icon_2_dis);
//            binding.batteryMeterView.setChargeLevel(Integer.valueOf(batteryPercent));
            TextView textView4 = binding.charingLevel;
            textView4.setText(batteryPercent + "%");
            binding.cahrgingStatus.setText("Charged");
        }
        binding.temp.setText((CharSequence) Paper.book().read("Temperature", "0°C"));
        binding.icon3.setImageResource(R.drawable.icon_3_temp);
        binding.crossBtn.setOnClickListener(view -> finish());
        binding.crossBtnBoost.setOnClickListener(view -> finish());

        loadNative();
    }


    private class getAppData extends AsyncTask<String, String, String> {
        private getAppData() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.boostedDialog.setVisibility(View.GONE);
        }


        public String doInBackground(String... strArr) {
            ChargeDialog chargeDialog = ChargeDialog.this;
            chargeDialog.app_data = chargeDialog.getInstalledApps();
            return null;
        }


        public void onPostExecute(String str) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (ChargeDialog.this.counter < 100) {
                        ChargeDialog.this.counter++;
                        ChargeDialog.this.memoryHandler.post(() -> {
                            if (ChargeDialog.this.counter == 100) {
                                for (int i = 0; i < ChargeDialog.this.app_data.size(); i++) {
                                    try {
                                        ((ActivityManager) ChargeDialog.this.getSystemService("activity")).killBackgroundProcesses(ChargeDialog.this.app_data.get(i));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                binding.boostedDialog.setVisibility(View.VISIBLE);
                                binding.forBoost.setVisibility(View.GONE);
                                binding.animationView.setVisibility(View.GONE);
                                binding.boostedDialog.setVisibility(View.VISIBLE);
                            }
                        });
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    public ArrayList<String> getInstalledApps() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);
            if (!isSystemPackage(packageInfo)) {
                arrayList.add(packageInfo.applicationInfo.packageName);
            }
        }
        return arrayList;
    }

    private static boolean isSystemPackage(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & 1) != 0;
    }

    int getBatteryPercent() {
        return registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("level", -1);
    }

    private void loadNative() {
        Admob.getInstance().loadNativeAd(this, "ca-app-pub-5839511744003864/6798764522", new NativeCallback() {
            @Override
            public void onAdFailedToLoad() {
                super.onAdFailedToLoad();
                binding.adsNativeChargingDialog.removeAllViews();
            }

            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                NativeAdView adView = (NativeAdView) LayoutInflater.from(ChargeDialog.this).inflate(R.layout.layout_native_screen_record, null);
                binding.adsNativeChargingDialog.removeAllViews();
                binding.adsNativeChargingDialog.addView(adView);
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView);
            }
        });
    }

}
