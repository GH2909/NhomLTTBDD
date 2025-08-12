package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class PaymentActivity extends BaseActivity {

    private TextView tvSummary;
    private MaterialButton btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_payment);

        tvSummary = findViewById(R.id.tvSummary);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Lấy tổng số món từ intent nếu cần
        int totalItems = getIntent().getIntExtra("total_items", 0);
        int totalPrice = totalItems * 120000; // Ví dụ mỗi món 120k
        tvSummary.setText("Tổng đơn hàng: " + totalPrice + "₫");

        btnConfirmPayment.setOnClickListener(v -> {
            // Gửi dữ liệu sang OrderHistoryActivity (bạn có thể lưu vào SharedPreferences, DB hoặc Intent)
            Intent intent = new Intent(PaymentActivity.this, OrderHistoryActivity.class);
            intent.putExtra("order_summary", "Đơn hàng: " + totalItems + " món, " + totalPrice + "₫");
            startActivity(intent);
            finish(); // Kết thúc activity hiện tại
        });
    }
}
