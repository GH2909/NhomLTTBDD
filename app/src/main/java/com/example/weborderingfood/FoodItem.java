package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

public class FoodItem {
    @SerializedName("ten_mon")
    private String name;
    @SerializedName("mo_ta")
    private String description;
    @SerializedName("gia")
    private double price; // Thay đổi kiểu dữ liệu từ int sang double
    @SerializedName("hinh_anh")
    private String imageUrl; // Sửa tên biến thành imageUrl để dễ đọc hơn

    // Constructor
    public FoodItem(String name, String description, double price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters
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

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}