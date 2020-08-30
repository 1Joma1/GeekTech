package com.joma.geektech.start;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joma.geektech.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences("data", MODE_PRIVATE).getBoolean("login", true)){
            RegisterActivity.start(this);
        } else {
            MainActivity.start(this);
        }
        finish();
    }
}
