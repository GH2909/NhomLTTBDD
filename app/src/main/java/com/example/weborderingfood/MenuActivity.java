package com.example.weborderingfood;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends BaseActivity implements CartUpdateListener {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private TextView tvCartItemCount;
    private EditText edtSearch;
    private ImageView btnSearch;
    private List<FoodItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_menu);

        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvCartItemCount = findViewById(R.id.tvCartItemCount);

        recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSearch.setOnClickListener(v -> performSearch());

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });

        String category = getIntent().getStringExtra("category");
        fetchFoodData(category);

        fetchCartItems();
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
                    }

                    if (adapter == null) {
                        adapter = new FoodAdapter(MenuActivity.this, foodList, MenuActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateData(foodList);
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

    private void performSearch() {
        String query = edtSearch.getText().toString().trim();
        if (query.isEmpty()) {
            String category = getIntent().getStringExtra("category");
            fetchFoodData(category);
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        // SỬA: Thay đổi tên phương thức và kiểu trả về thành FoodListResponse
        Call<FoodListResponse> call = apiService.searchFood(query);

        call.enqueue(new Callback<FoodListResponse>() {
            @Override
            public void onResponse(Call<FoodListResponse> call, Response<FoodListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // SỬA: Lấy danh sách món ăn từ đối tượng FoodListResponse
                    List<FoodItem> foodItems = response.body().getFoods();
                    Log.d("MenuActivity", "Kết quả tìm kiếm: Đã nhận " + foodItems.size() + " món.");

                    if (foodItems.isEmpty()) {
                        Toast.makeText(MenuActivity.this, "Không tìm thấy món ăn nào.", Toast.LENGTH_SHORT).show();
                    }

                    if (adapter == null) {
                        adapter = new FoodAdapter(MenuActivity.this, foodItems, MenuActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateData(foodItems);
                    }
                } else {
                    Toast.makeText(MenuActivity.this, "Không tìm thấy món ăn nào.", Toast.LENGTH_SHORT).show();
                    if (adapter != null) {
                        adapter.updateData(new ArrayList<>());
                    }
                }
            }

            @Override
            public void onFailure(Call<FoodListResponse> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCartItems() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CartApiResponse> call = apiService.getCartItems();
        call.enqueue(new Callback<CartApiResponse>() {
            @Override
            public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems = response.body().getCartItems();
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

    @Override
    public void onCartUpdated() {
        fetchCartItems();
    }
}
