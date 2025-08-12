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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_menu);

        String category = getIntent().getStringExtra("category");
        TextView tvTitle = findViewById(R.id.tvCategoryTitle);

        // Thêm logic để xử lý tiêu đề
        if (category != null && !category.isEmpty()) {
            tvTitle.setText("Danh mục: " + category);
        } else {
            tvTitle.setText("Toàn bộ món ăn");
        }


        recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchFoodData(category);
    }

    private void fetchFoodData(String category) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<FoodItem>> call;

        // Logic để gọi API phù hợp
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

                    // Xử lý trường hợp danh sách rỗng
                    if (foodList.isEmpty()) {
                        Toast.makeText(MenuActivity.this, "Không có món ăn nào trong danh mục này", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter = new FoodAdapter(MenuActivity.this, foodList);
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
}