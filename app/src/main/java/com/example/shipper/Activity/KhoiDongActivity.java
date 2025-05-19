package com.example.shipper.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KhoiDongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Mặc định là false

        if (isLoggedIn) {
            Intent intent = new Intent(KhoiDongActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(KhoiDongActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
