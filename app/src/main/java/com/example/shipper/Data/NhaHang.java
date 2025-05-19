package com.example.shipper.Data;

public class NhaHang {
    private String MaNH;
    private String TenNhaHang;
    private String TrangThaiNhaHang;
    private String TGhoatdong,DiaChi,LoaiNH;
    private int SoLuotBan;
    private String HinhAnh;

    public NhaHang() {
    }

    public NhaHang(String maNH, String tenNhaHang, String trangThaiNhaHang, String TGhoatdong, String diaChi, String loaiNH, int soLuotBan, String hinhAnh) {
        MaNH = maNH;
        TenNhaHang = tenNhaHang;
        TrangThaiNhaHang = trangThaiNhaHang;
        this.TGhoatdong = TGhoatdong;
        DiaChi = diaChi;
        LoaiNH = loaiNH;
        SoLuotBan = soLuotBan;
        HinhAnh = hinhAnh;
    }

    public NhaHang(String tenNhaHang, String trangThaiNhaHang, String TGhoatdong, String hinhAnh, String diaChi) {
        TenNhaHang = tenNhaHang;
        TrangThaiNhaHang = trangThaiNhaHang;
        this.TGhoatdong = TGhoatdong;
        HinhAnh = hinhAnh;
        DiaChi = diaChi;
    }

    public String getMaNH() {
        return MaNH;
    }

    public void setMaNH(String maNH) {
        MaNH = maNH;
    }

    public String getTenNhaHang() {
        return TenNhaHang;
    }

    public void setTenNhaHang(String tenNhaHang) {
        TenNhaHang = tenNhaHang;
    }

    public String getTrangThaiNhaHang() {
        return TrangThaiNhaHang;
    }

    public void setTrangThaiNhaHang(String trangThaiNhaHang) {
        TrangThaiNhaHang = trangThaiNhaHang;
    }

    public String getTGhoatdong() {
        return TGhoatdong;
    }

    public void setTGhoatdong(String TGhoatdong) {
        this.TGhoatdong = TGhoatdong;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getLoaiNH() {
        return LoaiNH;
    }

    public void setLoaiNH(String loaiNH) {
        LoaiNH = loaiNH;
    }

    public int getSoLuotBan() {
        return SoLuotBan;
    }

    public void setSoLuotBan(int soLuotBan) {
        SoLuotBan = soLuotBan;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
