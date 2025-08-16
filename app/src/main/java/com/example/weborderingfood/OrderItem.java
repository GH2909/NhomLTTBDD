package com.example.weborderingfood;

import java.io.Serializable;

/**
 * Lớp mô hình dữ liệu (Model) cho một món hàng trong đơn.
 * Lớp này cần phải được cài đặt Serializable để có thể truyền qua Intent giữa các Activity.
 */
public class OrderItem implements Serializable {
    private String productName;
    private int quantity;
    private double price;

    public OrderItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters cho các thuộc tính
    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
