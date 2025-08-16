package com.example.weborderingfood;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Lớp này được tạo ra để đại diện cho cấu trúc JSON trả về từ API tìm kiếm.
 * Định dạng: {"foods": [...]}
 */
public class FoodListResponse {

    @SerializedName("foods")
    private List<FoodItem> foods;

    public List<FoodItem> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodItem> foods) {
        this.foods = foods;
    }
}
