package com.example.weborderingfood;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// THAY ĐỔI: Triển khai interface CartUpdateListener
public class MenuActivity extends BaseActivity implements CartUpdateListener {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private TextView tvCartItemCount; // THÊM TextView để hiển thị số lượng
    private List<FoodItem> cartItems; // THÊM danh sách giỏ hàng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_menu);

        String category = getIntent().getStringExtra("category");
        TextView tvTitle = findViewById(R.id.tvCategoryTitle);
        tvCartItemCount = findViewById(R.id.tvCartItemCount); // Ánh xạ TextView từ layout

        // Thêm logic để xử lý tiêu đề
        if (category != null && !category.isEmpty()) {
            tvTitle.setText("Danh mục: " + category);
        } else {
            tvTitle.setText("Toàn bộ món ăn");
        }

        recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gọi các phương thức để tải dữ liệu
        fetchFoodData(category);
        fetchCartItems(); // THÊM: Tải giỏ hàng lần đầu
    }

    private void fetchFoodData(String category) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<List<FoodItem>> call;

        if (category != null && !category.isEmpty()) {
            call = apiService.getFoodByCategory(category, "true");
        } else {
            call = apiService.getAllFood("true");
        }

        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodItem> foodList = response.body();
                    Log.d("MenuActivity", "Đã nhận " + foodList.size() + " món ăn.");

                    if (foodList.isEmpty()) {
                        Toast.makeText(MenuActivity.this, "Không có món ăn nào trong danh mục này", Toast.LENGTH_SHORT).show();
                    } else {
                        // THAY ĐỔI: Tạo adapter với constructor mới, truyền 'this' làm listener
                        adapter = new FoodAdapter(MenuActivity.this, foodList, MenuActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(MenuActivity.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    Log.e("MenuActivity", "Lỗi: " + response.code() + ", " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MenuActivity", "Lỗi kết nối", t);
            }
        });
    }

    // THÊM: Phương thức để tải lại giỏ hàng từ server
    private void fetchCartItems() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CartApiResponse> call = apiService.getCartItems();
        call.enqueue(new Callback<CartApiResponse>() {
            @Override
            public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems = response.body().getCartItems();
                    // Cập nhật số lượng hiển thị trên TextView
                    if (tvCartItemCount != null) {
                        int totalItems = 0;
                        for (FoodItem item : cartItems) {
                            totalItems += item.getQuantity();
                        }
                        tvCartItemCount.setText(String.valueOf(totalItems));
                    }
                }
            }

            @Override
            public void onFailure(Call<CartApiResponse> call, Throwable t) {
                Log.e("API_CALL", "Lỗi tải giỏ hàng: " + t.getMessage());
            }
        });
    }

    // THÊM: Triển khai phương thức từ interface CartUpdateListener
    @Override
    public void onCartUpdated() {
        fetchCartItems(); // Khi FoodAdapter thông báo cập nhật, tải lại giỏ hàng
    }
}