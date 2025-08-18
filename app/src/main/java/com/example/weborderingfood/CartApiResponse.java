package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartApiResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("cartItems")
    private List<FoodItem> cartItems;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FoodItem> getCartItems() {
        return cartItems;
    }
}