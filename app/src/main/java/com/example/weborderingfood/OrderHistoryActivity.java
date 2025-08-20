package com.example.weborderingfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weborderingfood.OrderHistoryItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity implements OrderHistoryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistoryItem> orderList;
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerViewOrderHistory);
        emptyView = findViewById(R.id.emptyView);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, orderList, this);
        recyclerView.setAdapter(adapter);

        fetchOrderHistory();


    }

    private void fetchOrderHistory() {
        // Sử dụng các hằng số đã định nghĩa trong LoginActivity
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS_NAME, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(LoginActivity.KEY_USER_ID, -1);

        if (userId != -1) {
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<List<OrderHistoryItem>> call = apiService.getOrderHistory(userId);
            call.enqueue(new Callback<List<OrderHistoryItem>>() {
                @Override
                public void onResponse(Call<List<OrderHistoryItem>> call, Response<List<OrderHistoryItem>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        orderList.clear();
                        orderList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        updateEmptyView();
                    } else {
                        Log.e("OrderHistoryActivity", "Lỗi khi tải lịch sử đơn hàng: " + response.code());
                        Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
                        updateEmptyView();
                    }
                }

                @Override
                public void onFailure(Call<List<OrderHistoryItem>> call, Throwable t) {
                    Log.e("OrderHistoryActivity", "Lỗi kết nối: " + t.getMessage());
                    Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    updateEmptyView();
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử đơn hàng.", Toast.LENGTH_LONG).show();
            updateEmptyView();
        }
    }

    private void updateEmptyView() {
        if (orderList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(String orderId) {
        Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailActivity.class);
        intent.putExtra("order_id", orderId);
        startActivity(intent);
    }
}