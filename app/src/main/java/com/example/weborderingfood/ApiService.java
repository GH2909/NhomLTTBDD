package com.example.weborderingfood;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("menu.php")
    Call<List<FoodItem>> getFoodByCategory(@Query("category") String category, @Query("api") String api);

    @GET("menu.php")
    Call<List<FoodItem>> getAllFood(@Query("api") String api); // Thêm phương thức mới này
}