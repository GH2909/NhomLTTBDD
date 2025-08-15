package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;

public class CartRequest {
    @SerializedName("action")
    private String action;

    // SỬA LỖI Ở ĐÂY: "food" -> "food_id"
    @SerializedName("food_id")
    private int foodId;

    @SerializedName("quantity")
    private int quantity;

    public CartRequest(String action, int foodId, int quantity) {
        this.action = action;
        this.foodId = foodId;
        this.quantity = quantity;
    }
}