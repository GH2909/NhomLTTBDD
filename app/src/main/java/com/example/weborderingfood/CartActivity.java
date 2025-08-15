package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Khắc phục lỗi "No adapter attached" bằng cách khởi tạo adapter ngay lập tức
        cartAdapter = new CartAdapter(CartActivity.this, new ArrayList<>(), new Runnable() {
            @Override
            public void run() {
                updateTotalPrice(cartAdapter.getCartItems());
            }
        });
        recyclerViewCart.setAdapter(cartAdapter);

        fetchCartItems();

        btnCheckout.setOnClickListener(v -> {
            if (cartAdapter != null && cartAdapter.getItemCount() > 0) {
                // Lấy danh sách các món ăn hiện có trong giỏ hàng
                ArrayList<FoodItem> cartItems = new ArrayList<>(cartAdapter.getCartItems());

                // Tạo một Intent để chuyển đến OrderInformationActivity
                Intent intent = new Intent(CartActivity.this, OrderInformationActivity.class);

                // Gắn danh sách các món ăn vào Intent
                // Đảm bảo FoodItem đã implements Serializable
                intent.putExtra("cartItems", cartItems);

                // Bắt đầu Activity mới
                startActivity(intent);
            } else {
                Toast.makeText(this, "Giỏ hàng trống, không thể thanh toán", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCartItems() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CartApiResponse> call = apiService.getCartItems();

        call.enqueue(new Callback<CartApiResponse>() {
            @Override
            public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    List<FoodItem> cartItems = response.body().getCartItems();
                    if (cartItems != null) {
                        cartAdapter.updateData(cartItems);
                        updateTotalPrice(cartItems);
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Lỗi khi lấy dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartApiResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice(List<FoodItem> items) {
        double total = 0;
        for (FoodItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        tvTotalPrice.setText(formatter.format(total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCartItems();
    }
}