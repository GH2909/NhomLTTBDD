package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {
    @SerializedName("user_id") // THÊM DÒNG NÀY
    private int userId; // THÊM DÒNG NÀY

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("shipping_address")
    private String shippingAddress;

    @SerializedName("order_note")
    private String orderNote;

    @SerializedName("items")
    private List<FoodItem> items;

    @SerializedName("total_amount")
    private double totalAmount;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("order_date")
    private String orderDate;

    // CẬP NHẬT CONSTRUCTOR ĐỂ NHẬN userId
    public OrderRequest(int userId, String customerName, String phoneNumber, String shippingAddress, String orderNote, List<FoodItem> items, double totalAmount, String paymentMethod, String orderDate) {
        this.userId = userId; // GÁN GIÁ TRỊ userId
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.orderNote = orderNote;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
    }

    // Các getters (nếu cần)
    public int getUserId() {
        return userId;
    }
}