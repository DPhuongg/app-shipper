package com.example.shipper.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shipper.Adapter.DatHangAdapter;
import com.example.shipper.Data.ChiTietDonHang;
import com.example.shipper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChiTietDonHangDat extends AppCompatActivity {

    ImageView back;
    TextView diaChi, tongTien, phiVanChuyen, khuyenMai, thanhTien, tongThanhToan, tenNhaHang, ngaydat,ngaygiao;
    Button nhandon;
    RecyclerView viewDonDat;
    FirebaseFirestore firestore;
    Double TongTien, phish, km;
    DatHangAdapter datHangAdapter;
    ArrayList<ChiTietDonHang> lst;
    LinearLayout layout_vc;
    String makh1,maNH,madh ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();
        CollectionReference referenceChiTietGH = firestore.collection("ChiTietGioHang");
        CollectionReference referenceNH = firestore.collection("NhaHang");
        CollectionReference referenceShip = firestore.collection("Shipper");
        CollectionReference referenceDH = firestore.collection("DonHang");
        CollectionReference referenceCTDH = firestore.collection("ChiTietDonHang");
        CollectionReference referenceChiTietDH = firestore.collection("ChiTietDonHang");
        CollectionReference referenceKH = firestore.collection("KhachHang");

        // Tìm id
        back = findViewById(R.id.Back);
        diaChi = findViewById(R.id.tvDiaChiGH);
        tongTien = findViewById(R.id.TongTienHang);
        phiVanChuyen = findViewById(R.id.PhiVanChuyen);
        khuyenMai = findViewById(R.id.KhuyenMai);
        thanhTien = findViewById(R.id.ThanhTien);
        tongThanhToan = findViewById(R.id.TongThanhToan);
        nhandon = findViewById(R.id.btnDatDon);
        viewDonDat = findViewById(R.id.viewDonDat);
        tenNhaHang = findViewById(R.id.TenNhaHang);
        ngaydat = findViewById(R.id.tv_ngay_dat);
        ngaygiao = findViewById(R.id.tv_ngay_giao);


        // trở về
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChiTietDonHangDat.this, MainActivity.class));

                finish();
            }
        });

        // Lấy thông tin


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        TongTien = b.getDouble("TongTien");
        makh1 = b.getString("MaKhachHang");
        phish = b.getDouble("Phiship");
        km = b.getDouble("khuyenmai");
        maNH = b.getString("MaNhaHang");
        madh = b.getString("MaDon");

        NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
        String giaFormatted = format.format(TongTien);
        String phiVCformatted = format.format(phish);
        String KMformatted = format.format(km);

        tongTien.setText(giaFormatted + " đ");
        thanhTien.setText(giaFormatted + " đ");
        phiVanChuyen.setText(phiVCformatted + " đ");
        khuyenMai.setText(KMformatted + " đ");
        tongThanhToan.setText(giaFormatted + " đ");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewDonDat.setLayoutManager(layoutManager);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String masp = sharedPreferences.getString("MaShipper","");
        nhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChiTietDonHangDat.this)
                        .setTitle("Xác nhận đơn hàng")
                        .setMessage("Bạn có chắc chắn muốn xác nhận nhận đơn này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Tìm tài liệu đơn hàng có MaDon khớp với madon
                            referenceDH
                                    .whereEqualTo("MaDon", madh)
                                    .whereEqualTo("TrangThaiShip", "Chờ shipper xác nhận")
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            // Lấy tài liệu đầu tiên khớp với điều kiện
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                            // Cập nhật TrangThaiShip và KiemTraDonHang
                                            document.getReference().update("TrangThaiShip", "Shipper đã xác nhận","MaShipper",masp, "KiemTraDonHang", true)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("KiemTra", "Đã cập nhật thành công TrangThaiShip và KiemTraDonHang");
                                                        Toast.makeText(ChiTietDonHangDat.this, "Xác nhận đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("KiemTra", "Lỗi khi cập nhật TrangThaiShip và KiemTraDonHang", e);
                                                    });
                                        } else {
                                            Log.e("KiemTra", "Không tìm thấy đơn hàng hoặc điều kiện không phù hợp: ", task.getException());
                                        }
                                    });

                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
        referenceKH
                .whereEqualTo("MaKhachHang", makh1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot doc: snapshot) {
                                String ten = doc.getString("TenKhach");
                                String dc = doc.getString("DiaChi");
                                String soDienThoai = doc.getString("SoDienThoai");

                                diaChi.setText(ten + " | " + soDienThoai + "\n\n" + dc);
                            }
                        }
                        else {
                            Log.d("DatHangActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // tên nhà hàng
        referenceNH
                .whereEqualTo("MaNH", maNH)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot doc: snapshot) {
                                String tenNH = doc.getString("TenNhaHang");
                                tenNhaHang.setText(tenNH);
                            }
                        }
                        else {
                            Log.d("DatHangActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
        referenceDH
                .whereEqualTo("MaDon", madh)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot doc: snapshot) {
                                String ngaydatDH = doc.getString("ThoiGianDatHang");
                                ngaydat.setText("Ngày đặt đơn : "+ ngaydatDH);
                            }
                        }
                        else {
                            Log.d("DatHangActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
        ngaygiao.setVisibility(View.GONE);
        // ds món muốn đặt
        lst = new ArrayList<>();
        viewDonDat.setLayoutManager(new LinearLayoutManager(this));
        referenceChiTietDH
                .whereEqualTo("MaDon", madh)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if(snapshot != null) {
                                for(QueryDocumentSnapshot doc: snapshot) {
                                    ChiTietDonHang chiTietdonHang = doc.toObject(ChiTietDonHang.class);
                                    lst.add(chiTietdonHang);
                                }
                                Log.d("ChiTietDonHangDat", "So DH " + lst.size());
                                datHangAdapter = new DatHangAdapter(ChiTietDonHangDat.this, lst);
                                viewDonDat.setAdapter(datHangAdapter);
                                datHangAdapter.notifyDataSetChanged();
                            }
                        }

                        else {
                            Log.e("FirebaseError", "Lỗi khi truy vấn: ", task.getException());
                        }
                    }
                });



    }

}