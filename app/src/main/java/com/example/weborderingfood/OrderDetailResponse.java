package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// This is the data model for the API response.
// This class will map the JSON keys from the PHP file to Java fields.
public class OrderDetailResponse {

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("ten_nguoi_dat")
    private String tenNguoiDat; // New field for customer name

    @SerializedName("sdt")
    private String sdt; // New field for phone number

    @SerializedName("tong_tien")
    private String tongTien;

    @SerializedName("trang_thai")
    private String trangThai;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("ghi_chu")
    private String ghiChu; // New field for notes

    @SerializedName("dia_chi")
    private String diaChi; // New field for address

    @SerializedName("hinh_thuc_thanh_toan")
    private String phuongThucThanhToan; // New field for payment method

    @SerializedName("items")
    private List<OrderItem> items;

    // Nested class to represent each item in the order
    public static class OrderItem {
        @SerializedName("name")
        private String name;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("price")
        private String price;

        // Getters
        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getPrice() {
            return price;
        }
    }

    // Getters for all fields
    public String getOrderId() {
        return orderId;
    }

    public String getTenNguoiDat() {
        return tenNguoiDat;
    }

    public String getSdt() {
        return sdt;
    }

    public String getTongTien() {
        return tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
