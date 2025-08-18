package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryItem {

    @SerializedName("ma_don")
    private String orderId;

    @SerializedName("tong_tien")
    private String totalAmount;

    @SerializedName("trang_thai")
    private String status;

    @SerializedName("thoi_gian_dat")
    private String orderDate;

    // Constructors
    public OrderHistoryItem(String orderId, String totalAmount, String status, String orderDate) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}