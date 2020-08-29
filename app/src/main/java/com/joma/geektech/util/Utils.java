package com.joma.geektech.util;

import android.content.Context;
import android.widget.EditText;

public class Utils {
    public static String getText(EditText et){
        return et.getText().toString().trim();
    }

    public static String getPhone(Context context){
        return context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("phone", "");
    }
    public static String getProfile(Context context){
        return context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("profile", "");
    }
    public static void setUser(Context context, String phone, String profile){
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("phone", phone).apply();
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("profile", profile).apply();
    }
}
