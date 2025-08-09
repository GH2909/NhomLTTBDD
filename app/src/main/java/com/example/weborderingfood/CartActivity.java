package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class CartActivity extends BaseActivity {
    private MaterialButton btnPlus, btnMinus, btnPlus2, btnMinus2, btnPlus1, btnMinus1, btnCheckout;
    private TextView tvQuantity, tvQuantity1, tvQuantity2;
    private int quantity = 1, quantity1 = 1, quantity2 = 1;

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

        // Xử lý mặt hàng 1
        btnPlus.setOnClickListener(view -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                showToastMin();
            }
        });

        // Nút thanh toán → chuyển sang PayActivity
        btnCheckout.setOnClickListener(view -> {
            int totalItems = quantity ;

            // Hiển thị thông báo
            Toast.makeText(CartActivity.this,
                    "Bạn đã đặt " + totalItems + " món.",
                    Toast.LENGTH_LONG).show();

            // Chuyển màn hình
            Intent intent = new Intent(CartActivity.this, PayActivity.class);
            intent.putExtra("totalItems", totalItems); // Gửi số món sang PayActivity
            startActivity(intent);
        });
    }

    private void showToastMin() {
        Toast.makeText(CartActivity.this,
                "Số lượng tối thiểu là 1",
                Toast.LENGTH_SHORT).show();
    }
}
