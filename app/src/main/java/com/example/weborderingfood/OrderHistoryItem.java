package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryItem {

    @SerializedName("ma_don")
    private String orderId;

    @SerializedName("tong_tien")
    private String totalAmount;

    @SerializedName("trang_thai")
    private String status;

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
}
