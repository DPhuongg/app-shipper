package com.example.shipper.Data;

public class ChiTietDonHang {
    private String MaMon, Anh, TenMon, MaDon;
    private double Gia, SoLuong;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(String maMon, String anh, String tenMon, String maDon, double gia, double soLuong) {
        MaMon = maMon;
        Anh = anh;
        TenMon = tenMon;
        MaDon = maDon;
        Gia = gia;
        SoLuong = soLuong;
    }

    public String getMaMon() {
        return MaMon;
    }

    public void setMaMon(String maMon) {
        MaMon = maMon;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public String getMaDon() {
        return MaDon;
    }

    public void setMaDon(String maDon) {
        MaDon = maDon;
    }

    public double getGia() {
        return Gia;
    }

    public void setGia(double gia) {
        Gia = gia;
    }

    public double getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(double soLuong) {
        SoLuong = soLuong;
    }
}
