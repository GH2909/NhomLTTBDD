package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.io.Serializable;

public class OrderRequest implements Serializable {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("shipping_address")
    private String shippingAddress;

    @SerializedName("ward")
    private String ward;

    @SerializedName("region")
    private String region;

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

    public OrderRequest(int userId, String customerName, String phoneNumber, String shippingAddress, String ward, String region, String orderNote, List<FoodItem> items, double totalAmount, String paymentMethod, String orderDate) {
        this.userId = userId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.ward = ward;
        this.region = region;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getWard() {
        return ward;
    }

    public String getRegion() {
        return region;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
