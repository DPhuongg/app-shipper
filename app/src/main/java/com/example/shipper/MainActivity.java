package com.example.shipper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigation;
    ImageView menu;
    TextView tenShip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_navigation);


        // Tìm id
        drawerLayout = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        menu = findViewById(R.id.menu);
        tenShip = findViewById(R.id.tenShip);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.nhandon) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));

                    finish();
                }
                else if(id == R.id.dhdg) {
                    startActivity(new Intent(MainActivity.this, DonHangDangGiaoActivity.class));

                    finish();
                }
                else if(id == R.id.LichSuDH) {
                    startActivity(new Intent(MainActivity.this, LichSuDonHangActivity.class));

                    finish();
                }
                else {
                    SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(MainActivity.this, DangNhapActivity.class));

                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
}