package com.example.weborderingfood;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<FoodItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<FoodItem> getCartItems() {
        return cartItems;
    }

    public void addItemToCart(FoodItem item) {
        FoodItem existingItem = findItemInCart(item);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            item.setQuantity(1);
            cartItems.add(item);
        }
    }

    public void updateItemQuantity(FoodItem item, int quantity) {
        FoodItem existingItem = findItemInCart(item);
        if (existingItem != null) {
            existingItem.setQuantity(quantity);
        }
    }

    public void removeItemFromCart(FoodItem item) {
        cartItems.remove(item);
    }

    public void clearCart() {
        cartItems.clear();
    }

    private FoodItem findItemInCart(FoodItem item) {
        for (FoodItem cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                return cartItem;
            }
        }
        return null;
    }
}