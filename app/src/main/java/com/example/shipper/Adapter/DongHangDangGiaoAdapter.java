package com.example.shipper.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shipper.Activity.ChiTietDonHangDat;
import com.example.shipper.Activity.ChiTietDonHangGiao;
import com.example.shipper.Activity.DonHangDangGiaoActivity;
import com.example.shipper.Activity.MainActivity;
import com.example.shipper.Data.DonHang;
import com.example.shipper.Data.NhaHang;
import com.example.shipper.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DongHangDangGiaoAdapter extends RecyclerView.Adapter<DongHangDangGiaoAdapter.DongHangDangGiaoViewHolder>{
    private Context context;
    private List<DonHang> listdh;
    FirebaseFirestore firestore;
    CollectionReference referenceCTDH,referenceNH,referenceDH;
    String makh = "",manh,madon, masp, trangthaidon;
    Double tongtien ,phiship,khuyenmai;
    public DongHangDangGiaoAdapter(Context context, List<DonHang> listdh1) {
        this.context = context;
        this.listdh = listdh1;
        firestore = FirebaseFirestore.getInstance();
        referenceCTDH = firestore.collection("ChiTietDonHang");
        referenceNH = firestore.collection("NhaHang");
        referenceDH = firestore.collection("DonHang");
        Log.d("KiemTra", "So DH Adapter  " + listdh.size());
        // Log tất cả các MaDon trong danh sách
        for (int i = 0; i < listdh.size(); i++) {
            String maDon = listdh.get(i).getMaDon();
            Log.d("KiemTra", "Đơn hàng " + i + ": MaDon = " + maDon);
        }

    }
    @NonNull
    @Override
    public DongHangDangGiaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_hang, parent, false);
        return new DongHangDangGiaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DongHangDangGiaoAdapter.DongHangDangGiaoViewHolder holder, int position) {
        DonHang dh = listdh.get(position);
        makh = dh.getMaKH();
        tongtien = dh.getTongTien();
        phiship = dh.getPhiShip();
        khuyenmai = dh.getKhuyenMai();
        manh = dh.getMaNH();
        madon = dh.getMaDon();
        trangthaidon = dh.getTrangThai();

        holder.ttdon.setText("Trạng thái đơn :"+trangthaidon);
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);
        masp = sharedPreferences.getString("MaShipper","");
        // Truy vấn Firestore để lấy danh sách ChiTietDonHang dựa vào MaDon
        referenceNH.whereEqualTo("MaNH", manh)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<NhaHang> nhahangs = new ArrayList<>();

                        // Lấy tất cả các tài liệu và thêm vào danh sách
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            NhaHang chiTiet = document.toObject(NhaHang.class);
                            nhahangs.add(chiTiet);
                        }

                        if (!nhahangs.isEmpty()) {
                            // Lấy phần tử đầu tiên trong danh sách
                            NhaHang firstChiTiet = nhahangs.get(0);

                            // Hiển thị dữ liệu lên các View của item trong RecyclerView
                            holder.tennh.setText(firstChiTiet.getTenNhaHang());
                            holder.vitrinh.setText(firstChiTiet.getDiaChi());

                            // Sử dụng Glide để tải hình ảnh nếu có
                            Glide.with(context)
                                    .load(firstChiTiet.getHinhAnh())
                                    .into(holder.ivha);
                        } else {
                            Log.d("KiemTra", "Không tìm thấy ChiTietDonHang nào cho MaDon: " + manh);
                        }
                    } else {
                        Log.e("KiemTra", "Lỗi khi truy vấn ChiTietDonHang: ", task.getException());
                    }
                });
        holder.ctd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietDonHangGiao.class);
                Bundle b = new Bundle();
                b.putString("MaNhaHang", manh);
                b.putString("MaKhachHang", makh);
                b.putString("MaDon", madon);
                b.putDouble("TongTien", tongtien);
                b.putDouble("Phiship", phiship);
                b.putDouble("khuyenmai", khuyenmai);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        holder.btnnhandon.setText("Hoàn Thành");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
        String thoigiangiaohang = dateFormat.format(new Date());
        holder.btnnhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận đơn hàng")
                        .setMessage("Bạn có chắc chắn muốn xác nhận hoàn thành đơn này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Tìm tài liệu đơn hàng có MaDon khớp với madon
                            referenceDH
                                    .whereEqualTo("MaDon", madon)
                                    .whereEqualTo("MaShipper", masp)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            // Lấy tài liệu đầu tiên khớp với điều kiện
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                            // Cập nhật TrangThaiShip và KiemTraDonHang
                                            document.getReference().update("TrangThaiShip", "Hoàn thành","ThoiGianGiaoHang",thoigiangiaohang)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("KiemTra", "Đã cập nhật thành công TrangThaiShip và KiemTraDonHang");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("KiemTra", "Lỗi khi cập nhật TrangThaiShip và KiemTraDonHang", e);
                                                    });
                                        } else {
                                            Log.e("KiemTra", "Không tìm thấy đơn hàng hoặc điều kiện không phù hợp: ", task.getException());
                                        }
                                    });
                            ((Activity) context).startActivity(new Intent(context, DonHangDangGiaoActivity.class));

                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

    }
        @Override
        public int getItemCount() {
            return listdh.size();
        }

        public static class DongHangDangGiaoViewHolder extends RecyclerView.ViewHolder {
            ImageView ivha;
            TextView ctd, tennh, vitrinh, ttdon;
            Button btnnhandon;
            public DongHangDangGiaoViewHolder(@NonNull View itemView) {
                super(itemView);
                ivha = itemView.findViewById(R.id.anh);
                ctd = itemView.findViewById(R.id.ctdh);
                tennh = itemView.findViewById(R.id.TenNH);
                vitrinh = itemView.findViewById(R.id.ViTriNH);
                ttdon = itemView.findViewById(R.id.TongTienDon);
                btnnhandon = itemView.findViewById(R.id.btnND);
            }
        }
}
