//package com.example.weborderingfood;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.weborderingfood.model.OrderHistoryItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class OrderHistoryActivity extends BaseActivity implements OrderHistoryAdapter.OnItemClickListener {
//
//    private RecyclerView recyclerView;
//    private OrderHistoryAdapter adapter;
//    private List<OrderHistoryItem> orderList;
//    private TextView emptyView;
//    private ImageView backIcon;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentLayout(R.layout.activity_order_history);
//
//        recyclerView = findViewById(R.id.recyclerViewOrderHistory);
//        emptyView = findViewById(R.id.emptyView);
//        backIcon = findViewById(R.id.backIcon);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        orderList = new ArrayList<>();
//        adapter = new OrderHistoryAdapter(this, orderList, this);
//        recyclerView.setAdapter(adapter);
//
//        fetchOrderHistory();
//
//        backIcon.setOnClickListener(v -> finish());
//    }
//
//    private void fetchOrderHistory() {
//        ApiService apiService = ApiClient.getRetrofitInstance(this).create(ApiService.class);
//        Call<List<OrderHistoryItem>> call = apiService.getOrderHistory(); // Giả sử API trả về List<OrderHistoryItem>
//        call.enqueue(new Callback<List<OrderHistoryItem>>() {
//            @Override
//            public void onResponse(Call<List<OrderHistoryItem>> call, Response<List<OrderHistoryItem>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    orderList.clear();
//                    orderList.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//                    updateEmptyView();
//                } else {
//                    Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<OrderHistoryItem>> call, Throwable t) {
//                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateEmptyView() {
//        if (orderList.isEmpty()) {
//            emptyView.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        } else {
//            emptyView.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onItemClick(String orderId) {
//        Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailsActivity.class);
//        intent.putExtra("order_id", orderId);
//        startActivity(intent);
//    }
//}
