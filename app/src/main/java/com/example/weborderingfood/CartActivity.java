package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weborderingfood.BaseActivity;

public class CartActivity extends BaseActivity {

    private ImageButton btnPlus, btnMinus;
    private Button btnCheckout; // đổi từ MaterialButton sang Button
    private TextView tvQuantity;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_cart);

        // Ánh xạ view
        btnPlus     = findViewById(R.id.btnPlus);
        btnMinus    = findViewById(R.id.btnMinus);
        tvQuantity  = findViewById(R.id.tvQuantity);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Gán giá trị ban đầu
        tvQuantity.setText(String.valueOf(quantity));

        // Xử lý nút cộng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Xử lý nút trừ
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                showToastMin();
            }
        });

        // Xử lý nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            int totalItems = quantity; // Tạm thời chỉ 1 món
            Toast.makeText(this,
                    "Bạn đã đặt " + totalItems + " món.",
                    Toast.LENGTH_LONG).show();

            // Chuyển sang màn thanh toán
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra("totalItems", totalItems);
            startActivity(intent);
        });
    }

    private void showToastMin() {
        Toast.makeText(this,
                "Số lượng tối thiểu là 1",
                Toast.LENGTH_SHORT).show();
    }
}
