package com.example.weborderingfood;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("menu.php")
    Call<List<FoodItem>> getFoodByCategory(@Query("category") String category, @Query("api") String api);

    @GET("menu.php")
    Call<List<FoodItem>> getAllFood(@Query("api") String api);

    // ====================================================================
    // CÁC PHƯƠNG THỨC MỚI ĐỂ QUẢN LÝ GIỎ HÀNG
    // ====================================================================

    @POST("cart.php?api=true")
    Call<CartApiResponse> addItemToCart(@Body CartRequest request);

    @GET("cart.php?action=getCart&api=true")
    Call<CartApiResponse> getCartItems();

    @POST("cart.php?api=true")
    Call<CartApiResponse> updateItemQuantity(@Body CartRequest request);

    @POST("cart.php?api=true")
    Call<CartApiResponse> removeItemFromCart(@Body CartRequest request);

    //@GET("php/order/history.php")
    //Call<List<com.example.weborderingfood.model.OrderHistoryItem>> getOrderHistory();

    // ====================================================================
    // THÊM PHƯƠNG THỨC TẠO ĐƠN HÀNG MỚI
    // ====================================================================

    @POST("php/order/create.php?api=true")
    Call<OrderResponse> createOrder(@Body OrderRequest orderRequest);

    @GET("php/food/search.php")
    Call<FoodListResponse> searchFood(@Query("q") String query);

    @GET("php/order/detail.php")
    Call<OrderDetail> getOrderDetails(@Query("order_id") String orderId);
}