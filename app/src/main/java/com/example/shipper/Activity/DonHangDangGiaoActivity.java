package com.example.shipper.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shipper.Adapter.DongHangDangGiaoAdapter;
import com.example.shipper.Data.DonHang;
import com.example.shipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DonHangDangGiaoActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigation;
    ImageView menu;
    RecyclerView lstDon;
    DongHangDangGiaoAdapter adapter;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_navigation_dhdg);

        firestore = FirebaseFirestore.getInstance();
        CollectionReference referenceShip = firestore.collection("Shipper");
        CollectionReference referenceDH = firestore.collection("DonHang");

        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String masp = preferences.getString("MaShipper", "");

        // tim id
        drawerLayout = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        menu = findViewById(R.id.menu);

        lstDon = findViewById(R.id.viewDon);
        lstDon.setLayoutManager(new LinearLayoutManager(this));

        referenceDH
                .whereEqualTo("TrangThaiShip", "Shipper đã xác nhận")
                .whereEqualTo("MaShipper", masp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DonHang> donHangList = new ArrayList<>();

                            for(QueryDocumentSnapshot doc : task.getResult()){
                                DonHang donHang = doc.toObject(DonHang.class);
                                donHangList.add(donHang);
                            }

                            Log.d("KiemTra", "So DH " + donHangList.size());

                            // Kiem tra neu co don hang
                            if(!donHangList.isEmpty()){
                                adapter = new DongHangDangGiaoAdapter(DonHangDangGiaoActivity.this, donHangList);
                                lstDon.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(DonHangDangGiaoActivity.this, "Không có đơn hàng mới", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(DonHangDangGiaoActivity.this, "Lỗi khi tải đơn hàng: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
                    startActivity(new Intent(DonHangDangGiaoActivity.this, MainActivity.class));

                    finish();
                }
                else if(id == R.id.dhdg) {
                    startActivity(new Intent(DonHangDangGiaoActivity.this, DonHangDangGiaoActivity.class));

                    finish();
                }
                else {
                    SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(DonHangDangGiaoActivity.this, DangNhapActivity.class));

                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
}