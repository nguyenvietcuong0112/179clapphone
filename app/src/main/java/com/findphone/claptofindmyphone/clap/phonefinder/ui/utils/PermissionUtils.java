package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.google.android.material.snackbar.Snackbar;

public class PermissionUtils {

    public static String[] RECORD_AUDIO_CAMERA_PERMISSION = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};
    public static String[] RECORD_AUDIO_PERMISSION = {Manifest.permission.RECORD_AUDIO,};

    public static String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA,};

    public static boolean recordAudioPermissionGrant(Context context) {
        return allPermissionGrant(context, getRecordAudioPermission());
    }

    public static boolean cameraPermissionGrant(Context context) {
        return allPermissionGrant(context, getCameraPermission());
    }

    private static String[] getRecordAudioPermission() {
        return RECORD_AUDIO_PERMISSION;
    }

    private static String[] getCameraPermission() {
        return CAMERA_PERMISSION;
    }

    private static boolean allPermissionGrant(Context context, String[] arrays) {
        boolean isGranted = true;
        for (String permission : arrays) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    public static void showAlertPermissionNotGrant(View view, Activity activity) {
        if (!hasShowRequestPermissionRationale(activity, getRecordAudioPermission())) {
            Snackbar snackBar = Snackbar.make(
                    view, activity.getResources().getString(R.string.goto_settings),
                    Snackbar.LENGTH_LONG
            );
            snackBar.setAction(
                    activity.getResources().getString(R.string.settings)

                    , view1 -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    });
            snackBar.show();
        } else {
            ToastUtils.getInstance(
                    activity
            ).showToast(activity.getResources().getString(R.string.grant_permission));
        }
    }

    public static boolean hasShowRequestPermissionRationale(
            Context context,
            String[] permissions
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        ((Activity) context),
                        permission
                )
                ) {
                    return true;
                }
            }
        }
        return false;
    }

}
