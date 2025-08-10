package com.example.weborderingfood;

public class FoodItem {
    private String tenmon, mota;
    private int gia, hinhanh;

    public FoodItem(String tenmon, String mota, int gia, int hinhanh) {
        this.tenmon = tenmon;
        this.mota = mota;
        this.gia = gia;
        this.hinhanh = hinhanh;
    }

    public String getTenmon() {
        return tenmon;
    }

    public String getMota() {
        return mota;
    }

    public int getGia() {
        return gia;
    }

    public int getHinhanh() {
        return hinhanh;
    }
}
