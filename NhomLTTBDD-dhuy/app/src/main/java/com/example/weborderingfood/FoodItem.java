package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

public class FoodItem {

    @SerializedName("id") // Đã sửa tên cột để khớp với database của bạn
    private int id;

    @SerializedName("ten_mon")
    private String name;

    @SerializedName("mo_ta")
    private String description;

    @SerializedName("gia")
    private double price;

    @SerializedName("hinh_anh")
    private String imageUrl;

    @SerializedName("loai")
    private String category;

    private int quantity;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}