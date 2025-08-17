package com.example.weborderingfood;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Model class for an order item, created from a JSON object.
 * This class maps the JSON keys from the provided detail.php script.
 */
public class OrderItem implements Serializable {
    private String productName;
    private int quantity;
    private double price;

    public OrderItem(JSONObject jsonObject) throws JSONException {
        // Map data from the JSON Object
        this.productName = jsonObject.getString("product_name");
        this.quantity = jsonObject.getInt("quantity");
        this.price = jsonObject.getDouble("price");
    }

    // Getters for all fields
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
