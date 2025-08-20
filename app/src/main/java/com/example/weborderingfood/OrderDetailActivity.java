package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weborderingfood.Constants;
import com.google.gson.Gson;
import com.example.weborderingfood.OrderDetailResponse;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Activity to display details of a specific order.
 * Fetches order details from the server via an API call
 * and populates the UI with the received data.
 */
public class OrderDetailActivity extends BaseActivity {

    private TextView tvOrderId, tvCustomerName, tvPhoneNumber, tvTotal, tvStatus, tvCreatedAt, tvNotes, tvAddress, tvPaymentMethod;
    private RecyclerView rvItems;
    private Button btnContinueShopping, btnViewHistory;
    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_detail);

        // Ánh xạ các TextView từ XML
        tvOrderId = findViewById(R.id.tv_order_id);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvTotal = findViewById(R.id.tv_total);
        tvStatus = findViewById(R.id.tv_status);
        tvCreatedAt = findViewById(R.id.tv_created_at);
        tvNotes = findViewById(R.id.tv_notes);
        tvAddress = findViewById(R.id.tv_address);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);

        // Ánh xạ các nút chức năng
        btnContinueShopping = findViewById(R.id.btn_continue_shopping);
        btnViewHistory = findViewById(R.id.btn_view_history);

        // Xử lý sự kiện click cho các nút
        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(OrderDetailActivity.this, "Tiếp tục mua hàng!", Toast.LENGTH_SHORT).show();
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
            Toast.makeText(OrderDetailActivity.this, "Xem lịch sử đơn hàng!", Toast.LENGTH_SHORT).show();
        });

        // Cấu hình RecyclerView
        rvItems = findViewById(R.id.rv_order_items);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        String orderId = getIntent().getStringExtra("order_id");

        if (orderId != null && !orderId.isEmpty()) {
            fetchOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy mã đơn hàng.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Fetches order details from the server via an API call.
     *
     * @param orderId The ID of the order to fetch.
     */
    private void fetchOrderDetail(String orderId) {
        OkHttpClient client = new OkHttpClient();

        String url = Constants.BASE_URL + "php/order/detail.php?order_id=" + orderId;

        Log.d("OrderDetailActivity", "Đang lấy URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("OrderDetailActivity", "Lỗi kết nối: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Log.d("OrderDetailActivity", "Phản hồi: " + jsonResponse);

                    try {
                        Gson gson = new Gson();
                        final OrderDetailResponse orderDetail = gson.fromJson(jsonResponse, OrderDetailResponse.class);

                        runOnUiThread(() -> {
                            if (orderDetail != null) {
                                // Cập nhật các TextView với dữ liệu từ phản hồi
                                tvOrderId.setText( "#" + orderDetail.getOrderId());
                                tvCustomerName.setText(orderDetail.getTenNguoiDat());
                                tvPhoneNumber.setText( orderDetail.getSdt());
                                tvTotal.setText( orderDetail.getTongTien());
                                tvStatus.setText( orderDetail.getTrangThai());
                                tvCreatedAt.setText( orderDetail.getCreatedAt());
                                tvNotes.setText( orderDetail.getGhiChu());
                                tvAddress.setText( orderDetail.getDiaChi());
                                tvPaymentMethod.setText(orderDetail.getPhuongThucThanhToan());

                                List<OrderDetailResponse.OrderItem> items = orderDetail.getItems();
                                if (items != null && !items.isEmpty()) {
                                    // Khởi tạo và gán adapter cho RecyclerView
                                    OrderItemsAdapter adapter = new OrderItemsAdapter(items);
                                    rvItems.setAdapter(adapter);
                                } else {
                                    // Hiển thị thông báo nếu danh sách món ăn rỗng
                                    Toast.makeText(OrderDetailActivity.this, "Không có món ăn nào trong đơn hàng.", Toast.LENGTH_SHORT).show();
                                    Log.d("OrderDetailActivity", "Danh sách món ăn trống.");
                                }
                            } else {
                                Toast.makeText(OrderDetailActivity.this, "Không tìm thấy thông tin đơn hàng.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(OrderDetailActivity.this, "Không tìm thấy đơn hàng với mã ID này.", Toast.LENGTH_LONG).show();
                            Log.e("OrderDetailActivity", "Lỗi cú pháp JSON: " + e.getMessage());
                            Log.e("OrderDetailActivity", "Phản hồi server không hợp lệ: " + jsonResponse);
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(OrderDetailActivity.this, "Lỗi khi lấy dữ liệu: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("OrderDetailActivity", "Lỗi phản hồi từ server: " + response.code());
                    });
                }
            }
        });
    }
}
