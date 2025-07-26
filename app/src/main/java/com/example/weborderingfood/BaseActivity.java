package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected ImageButton btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        super.setContentView(R.layout.activity_base);
        drawerLayout = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupMenu();
    }

    public void setContentLayout(int layoutResId) {
        FrameLayout frameLayout = findViewById(R.id.contentFrame);
        LayoutInflater.from(this).inflate(layoutResId, frameLayout, true);
    }
    private void setupMenu(){
        String[] menuItems = {"Trang chủ", "Menu", "Lịch sử đơn hàng", "Đăng nhập"};
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        ListView menu = findViewById(R.id.menu);

        menu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems));

        menu.setOnItemClickListener((adapterView, view, i, l) -> {
            drawerLayout.closeDrawers();
            switch (i) {
                case 0:
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                    break;
//                case 1:
//                    startActivity(new Intent(BaseActivity.this, MenuActivity.class));
//                    break;
//                case 2:
//                    startActivity(new Intent(BaseActivity.this, HistoryActivity.class));
//                    break;
                case 3:
                    startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                    break;

            }
        });
    }
}