package com.example.weborderingfood;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.material.button.MaterialButton;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.activity.EdgeToEdge;


public class CartActivity extends BaseActivity {
    private MaterialButton btnPlus, btnMinus, btnPlus2, btnMinus2, btnPlus1, btnMinus1, btnCheckout;
    private TextView tvQuantity, tvQuantity1, tvQuantity2;
    private int quantity = 1, quantity1 = 1, quantity2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_cart);
        // Ánh xạ view
        btnPlus     = findViewById(R.id.btnPlus);
        btnMinus    = findViewById(R.id.btnMinus);
        tvQuantity  = findViewById(R.id.tvQuantity);

//        btnPlus2    = findViewById(R.id.btnPlus2);
//        btnMinus2   = findViewById(R.id.btnMinus2);
//        tvQuantity2 = findViewById(R.id.tvQuantity2);
//
//        btnPlus1    = findViewById(R.id.btnPlus1);
//        btnMinus1   = findViewById(R.id.btnMinus1);
//        tvQuantity1 = findViewById(R.id.tvQuantity1);

        btnCheckout = findViewById(R.id.btnCheckout);

        // Gán giá trị ban đầu
        tvQuantity.setText(String.valueOf(quantity));
//        tvQuantity2.setText(String.valueOf(quantity2));
//        tvQuantity1.setText(String.valueOf(quantity1));

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

        // Mặt hàng 2
//        btnPlus1.setOnClickListener(view -> {
//            quantity1++;
//            tvQuantity1.setText(String.valueOf(quantity1));
//        });
//
//        btnMinus1.setOnClickListener(view -> {
//            if (quantity1 > 1) {
//                quantity1--;
//                tvQuantity1.setText(String.valueOf(quantity1));
//            } else {
//                showToastMin();
//            }
//        });
//
//        // Mặt hàng 3
//        btnPlus2.setOnClickListener(view -> {
//            quantity2++;
//            tvQuantity2.setText(String.valueOf(quantity2));
//        });
//
//        btnMinus2.setOnClickListener(view -> {
//            if (quantity2 > 1) {
//                quantity2--;
//                tvQuantity2.setText(String.valueOf(quantity2));
//            } else {
//                showToastMin();
//            }
//        });

        btnCheckout.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("total_items", quantity);
            startActivity(intent);
        });
    }

    private void showToastMin() {
        Toast.makeText(CartActivity.this,
                "Số lượng tối thiểu là 1",
                Toast.LENGTH_SHORT).show();
    }
}