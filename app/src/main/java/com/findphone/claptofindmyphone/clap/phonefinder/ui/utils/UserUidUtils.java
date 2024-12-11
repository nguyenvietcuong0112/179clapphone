package com.findphone.claptofindmyphone.clap.phonefinder.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Random;

public class UserUidUtils {
    private static SharedPreferences sharePref;

    public static void init(Context context) {
        if (sharePref == null) {
            sharePref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        }
    }

    public static String getUserUuid() {
        return sharePref != null ? sharePref.getString("user_uuid", "") : "";
    }

    public static String getUserUuid(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getString("user_uuid", "");
    }

    private static String getRandomString(int length) {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(index));
        }
        return sb.toString();
    }

    public static String generateUserUuid(Context context) {
        String randomUuid = getRandomString(8);
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("user_uuid", randomUuid);
        editor.apply();
        return randomUuid;
    }

    public static void updateUserUuid(Context context, String firebaseAuthId) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("user_uuid", firebaseAuthId);
        editor.apply();
    }
}
