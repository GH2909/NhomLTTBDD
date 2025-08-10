package com.example.weborderingfood;

import android.os.Bundle;

public class OrderHistoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_order_history); // Chèn nội dung layout riêng vào khung
        setTitle("Lịch sử đơn hàng"); // Tùy chọn đặt tiêu đề
    }
}
