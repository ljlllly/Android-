package com.example.foodorderapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_splash);
        } catch (Exception e) {
            Log.e(TAG, "setContentView failed", e);
        }

        new Handler().postDelayed(() -> {
            try {
                SharedPreferences prefs = getSharedPreferences("food_order_prefs", MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);

                Intent intent;
                if (userId != -1) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Navigation failed", e);
                // 无论如何都跳转到登录页
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
