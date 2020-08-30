package com.joma.geektech.util;

import android.content.Context;
import android.widget.EditText;

import com.google.gson.Gson;
import com.joma.geektech.model.User;

public class Utils {
    public static String getText(EditText et){
        return et.getText().toString().trim();
    }

    public static User getUser(Context context){
        return new Gson().fromJson(context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("user", ""), User.class);
    }

    public static void setUser(Context context, User user){
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("user", new Gson().toJson(user)).apply();
    }
    public static void clear(Context context){
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
