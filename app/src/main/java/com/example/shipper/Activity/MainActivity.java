package com.example.shipper.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shipper.Adapter.DonHangAdapter;
import com.example.shipper.Data.DonHang;
//import com.example.shipper.Data.Shipper;
import com.example.shipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    NavigationView navigation;
    ImageView menu, anhShip;
    TextView tenShip;
    RecyclerView lstDon;
    DonHangAdapter adapter;
    int a ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation);

        firestore = FirebaseFirestore.getInstance();
        CollectionReference referenceShip = firestore.collection("Shipper");
        CollectionReference referenceDH = firestore.collection("DonHang");

        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String tenDangNhap = preferences.getString("TenDangNhap", "");
        String mas = preferences.getString("MaShipper", "");
        // Tìm id
        drawerLayout = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        menu = findViewById(R.id.menu);
        anhShip = findViewById(R.id.ivHA);
        tenShip = findViewById(R.id.tenShip);
        lstDon = findViewById(R.id.viewDon);
        lstDon.setLayoutManager(new LinearLayoutManager(this));


        if(a == 1){
            loaddata();
        }else if(a == 0){
            Toast.makeText(MainActivity.this, "Không có đơn hàng mới", Toast.LENGTH_SHORT).show();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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


        String maship = preferences.getString("MaShipper", "");


        referenceShip.whereEqualTo("MaShipper", maship)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if(snapshot != null) {
                                for(QueryDocumentSnapshot doc: snapshot) {
                                    Switch trangThaiS = findViewById(R.id.TrangThai);
                                    String ten = doc.getString("TenShipper");
                                    String anh = doc.getString("Anh");
                                    Log.d("KTAnh","url "+ anh);
                                    boolean trangThai = doc.getBoolean("TrangThai");
                                    if(trangThai == true){
                                        a = 1;
                                        loaddata();
                                    }else{
                                        a = 0;
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Thông báo")
                                                .setMessage("Bạn cần bật trạng thái hoạt động để nhận đơn !")
                                                .setPositiveButton("OK", (dialog, which) -> {
                                                    drawerLayout.openDrawer(GravityCompat.START);
                                                })
                                                .setNegativeButton("Không", null)
                                                .show();
                                    }
                                    trangThaiS.setChecked(trangThai);
                                    tenShip.setText(ten);

                                    TextView tenS = findViewById(R.id.hoTen);
                                    tenS.setText(ten);


                                    //trangThaiS.setChecked(trangThai);
                                    trangThaiS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            // Kiểm tra trạng thái của Switch
                                            if (isChecked) {

                                                a = 1;
                                                referenceShip
                                                        .whereEqualTo("MaShipper", mas)
                                                        .get()
                                                        .addOnCompleteListener(task -> {
                                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                document.getReference().update("TrangThai", true)
                                                                        .addOnSuccessListener(aVoid -> {
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                        });
                                                            } else {
                                                            }
                                                        });
                                                loaddata();
                                            } else {

                                                a = 0;
                                                referenceShip
                                                        .whereEqualTo("MaShipper", mas)
                                                        .get()
                                                        .addOnCompleteListener(task -> {
                                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                                document.getReference().update("TrangThai", false)
                                                                        .addOnSuccessListener(aVoid -> {
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                        });
                                                            } else {
                                                            }
                                                        });
                                                List<DonHang> donHangList = new ArrayList<>();
                                                adapter = new DonHangAdapter(MainActivity.this,donHangList);
                                                lstDon.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();

                                            }
                                            Log.d("KTa","a = "+a);
                                        }
                                    });

                                    ImageView anhS = findViewById(R.id.avatar);

                                    if (anh != null && !anh.isEmpty()) {
                                        Glide.with(MainActivity.this).load(anh).into(anhShip);
                                        Glide.with(MainActivity.this).load(anh).into(anhS);
                                    }

                                    else {
                                        anhShip.setImageResource(R.drawable.doraemon);
                                        anhS.setImageResource(R.drawable.doraemon);
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi khi lấy dữ liệu người dùng: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void loaddata(){
        CollectionReference referenceDH = firestore.collection("DonHang");
        referenceDH.whereEqualTo("TrangThaiShip", "Chờ shipper xác nhận")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DonHang> donHangList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                DonHang donHang = doc.toObject(DonHang.class);
                                donHangList.add(donHang);
                            }
                            for (int i = 0; i < donHangList.size(); i++) {
                                String maDon = donHangList.get(i).getMaDon();
                                String maKH = donHangList.get(i).getMaKH();
                                Log.d("KiemTra", "Đơn hàng main " + i + ": MaDon = " + maDon + " makh : "+ maKH);
                            }
                            Log.d("KiemTra", "So DH " + donHangList.size());
                            // Kiểm tra nếu có đơn hàng
                            if (!donHangList.isEmpty()) {
                                adapter = new DonHangAdapter(MainActivity.this,donHangList);
                                lstDon.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MainActivity.this, "Không có đơn hàng mới", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Lỗi khi tải đơn hàng: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}