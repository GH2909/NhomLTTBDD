package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("message")
    private String message;

    // Getters
    public String getStatus() {
        return status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }
}