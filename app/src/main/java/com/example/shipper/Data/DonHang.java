package com.example.shipper.Data;

public class DonHang {
    private String MaDon, MaNH, MaKH, TenShipper, TrangThai, PhuongThucTT, TrangThaiShip;
    private double TongTien, KhuyenMai, PhiShip;
    private boolean KiemTraDonHang;

    public DonHang() {
    }

    public DonHang(String maDon, String maNH, String maKH, String trangThai, String trangThaiShip, double khuyenMai, double phiShip, boolean kiemTraDonHang) {
        MaDon = maDon;
        MaNH = maNH;
        MaKH = maKH;
        TrangThai = trangThai;
        TrangThaiShip = trangThaiShip;
        KhuyenMai = khuyenMai;
        PhiShip = phiShip;
        KiemTraDonHang = kiemTraDonHang;
    }

    public DonHang(String maDon, String maNH, String maKH, String tenShipper, String trangThai, String phuongThucTT, String trangThaiShip, double tongTien, double khuyenMai, double phiShip, boolean kiemTraDonHang) {
        MaDon = maDon;
        MaNH = maNH;
        MaKH = maKH;
        TenShipper = tenShipper;
        TrangThai = trangThai;
        PhuongThucTT = phuongThucTT;
        TrangThaiShip = trangThaiShip;
        TongTien = tongTien;
        KhuyenMai = khuyenMai;
        PhiShip = phiShip;
        KiemTraDonHang = kiemTraDonHang;
    }

    public String getMaDon() {
        return MaDon;
    }

    public void setMaDon(String maDon) {
        MaDon = maDon;
    }

    public String getMaNH() {
        return MaNH;
    }

    public void setMaNH(String maNH) {
        MaNH = maNH;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String maKH) {
        MaKH = maKH;
    }

    public String getTenShipper() {
        return TenShipper;
    }

    public void setTenShipper(String tenShipper) {
        TenShipper = tenShipper;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public String getPhuongThucTT() {
        return PhuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        PhuongThucTT = phuongThucTT;
    }

    public String getTrangThaiShip() {
        return TrangThaiShip;
    }

    public void setTrangThaiShip(String trangThaiShip) {
        TrangThaiShip = trangThaiShip;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double tongTien) {
        TongTien = tongTien;
    }

    public double getKhuyenMai() {
        return KhuyenMai;
    }

    public void setKhuyenMai(double khuyenMai) {
        KhuyenMai = khuyenMai;
    }

    public double getPhiShip() {
        return PhiShip;
    }

    public void setPhiShip(double phiShip) {
        PhiShip = phiShip;
    }

    public boolean isKiemTraDonHang() {
        return KiemTraDonHang;
    }

    public void setKiemTraDonHang(boolean kiemTraDonHang) {
        KiemTraDonHang = kiemTraDonHang;
    }
}

