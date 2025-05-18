package com.example.shipper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shipper.Data.ChiTietDonHang;
import com.example.shipper.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DatHangAdapter extends RecyclerView.Adapter<DatHangAdapter.DatHangViewHolder> {

    Context context;
    ArrayList<ChiTietDonHang> lst;

    public DatHangAdapter(Context context, ArrayList<ChiTietDonHang> lst) {
        this.context = context;
        this.lst = lst;
    }

    @NonNull
    @Override
    public DatHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DatHangViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dat_hang, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DatHangViewHolder holder, int position) {
        ChiTietDonHang item = lst.get(position);

        holder.tenMon.setText(item.getTenMon());

        int sl = (int) item.getSoLuong();

        holder.soLuong.setText(String.valueOf(sl) + "x");
        Glide.with(context).load(item.getAnh()).into(holder.Anh);

        double giaMon = item.getGia();
        NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
        String giaFormatted = format.format(giaMon);
        holder.giaTien.setText(giaFormatted + " đ");
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    public static class DatHangViewHolder extends RecyclerView.ViewHolder {

        ImageView Anh;
        TextView soLuong, tenMon, giaTien;

        public DatHangViewHolder(@NonNull View itemView) {
            super(itemView);

            Anh = itemView.findViewById(R.id.Anh);
            soLuong = itemView.findViewById(R.id.SoLuong);
            tenMon = itemView.findViewById(R.id.TenMon);
            giaTien = itemView.findViewById(R.id.GiaTien);
        }
    }
}
