package com.example.shipper.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.shipper.Activity.DonHangDangGiaoActivity;
import com.example.shipper.Data.DonHang;
import com.example.shipper.Data.NhaHang;
import com.example.shipper.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DongHangDangGiaoAdapter extends RecyclerView.Adapter<DongHangDangGiaoAdapter.DongHangDangGiaoViewHolder>{
    private Context context;
    private List<DonHang> listdh;
    FirebaseFirestore firestore;
    CollectionReference referenceCTDH,referenceNH,referenceDH;
    String makh = "",manh,madon, masp, trangthaidon;

    public DongHangDangGiaoAdapter(Context context, List<DonHang> listdh1) {
        this.context = context;
        this.listdh = listdh1;
        firestore = FirebaseFirestore.getInstance();
        referenceCTDH = firestore.collection("ChiTietDonHang");
        referenceNH = firestore.collection("NhaHang");
        referenceDH = firestore.collection("DonHang");

    }
    @NonNull
    @Override
    public DongHangDangGiaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_hang, parent, false);
        return new DongHangDangGiaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DongHangDangGiaoViewHolder holder, int position) {
        DonHang dh = listdh.get(position);
        makh = dh.getMaKH();
        madon = dh.getMaDon();
        manh = dh.getMaNH();
        trangthaidon = dh.getTrangThai();

        String trangThai = dh.getTrangThai();

        if (!"Chờ giao hàng".equals(trangThai)) {
            holder.btnnhandon.setEnabled(false);
            holder.btnnhandon.setAlpha(0.5f); // làm mờ nút
        } else {
            holder.btnnhandon.setEnabled(true); // kích hoạt
            holder.btnnhandon.setAlpha(1.0f);
        }

        holder.ttdon.setText("Trạng thái đơn : " + trangthaidon);
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);
        masp = sharedPreferences.getString("MaShipper", "");

        referenceNH
                .whereEqualTo("MaNH", manh)
                .get()
                .addOnCompleteListener(task-> {
                    if(task.isSuccessful()){
                        List<NhaHang> nhaHangs = new ArrayList<>();

                        // add ban ghi nha hang
                        for(QueryDocumentSnapshot document : task.getResult()){
                            NhaHang nhaHang = document.toObject(NhaHang.class);
                            nhaHangs.add(nhaHang);
                        }

                        if(!nhaHangs.isEmpty()){
                            // Lấy phần tử đầu tiên trong danh sách
                            NhaHang nhaHangDauTien = nhaHangs.get(0);

                            // set du lieu len View
                            holder.tennh.setText(nhaHangDauTien.getTenNhaHang());
                            holder.vitriNH.setText(nhaHangDauTien.getDiaChi());

                            Glide.with(context)
                                    .load(nhaHangDauTien.getHinhAnh())
                                    .into(holder.ivha);
                        }else {
                            Log.d("KiemTra", "Không tìm thấy Nhà hàng nào: " + manh);
                        }
                    }else {
                        Log.e("KiemTra", "Lỗi khi truy vấn NhaHang: ", task.getException());
                    }
                });

        holder.btnnhandon.setText("Hoàn Thành");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - YYYY");
        String thoiGianGiaoHang = dateFormat.format(new Date());

        holder.btnnhandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận đơn hàng")
                        .setMessage("Bạn có chắc chắn muốn xác nhận hoàn thành đơn này không?\"")
                        .setPositiveButton("Có", ((dialog, which) -> {
                            referenceDH
                                    .whereEqualTo("MaDon", madon)
                                    .whereEqualTo("MaShipper", masp)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            // Lấy tài liệu đầu tiên khớp với điều kiện
                                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                            // Cập nhật TrangThaiShip và KiemTraDonHang
                                            document.getReference().update("TrangThaiShip", "Hoàn thành", "ThoiGianGiaoHang", thoiGianGiaoHang)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("KiemTra", "Đã cập nhật thành công TrangThaiShip");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("KiemTra", "Lỗi khi cập nhật TrangThaiShip", e);
                                                    });;

                                        } else {
                                            Log.e("KiemTra", "Không tìm thấy đơn hàng hoặc điều kiện không phù hợp: ", task.getException());
                                        }
                                    });
                            context.startActivity(new Intent(context, DonHangDangGiaoActivity.class));
                        }))
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
            TextView ctdh, tennh, vitriNH, ttdon;
            Button btnnhandon;

            public DongHangDangGiaoViewHolder(@NonNull View itemView) {
                super(itemView);
                ivha = itemView.findViewById(R.id.anh);
                ctdh = itemView.findViewById(R.id.ctdh);
                tennh = itemView.findViewById(R.id.TenNH);
                vitriNH = itemView.findViewById(R.id.ViTriNH);
                ttdon = itemView.findViewById(R.id.TongTienDon);
                btnnhandon = itemView.findViewById(R.id.btnND);
            }
        }
}
