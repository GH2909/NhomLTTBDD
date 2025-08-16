package com.example.weborderingfood;

import static com.example.weborderingfood.Constants.BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        updateMenu();
    }

    public void setContentLayout(int layoutResId) {
        FrameLayout frameLayout = findViewById(R.id.contentFrame);
        LayoutInflater.from(this).inflate(layoutResId, frameLayout, true);
    }
    private void updateMenu(){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("logged_in", false);
        String fullname = prefs.getString("fullname", "Chưa đăng nhập");
        Log.d("DEBUG", "logged_in = " + isLoggedIn);
        Log.d("DEBUG", "fullname = " + fullname);

        String[] menuItems;
        if (isLoggedIn) {
            // Thêm "Giỏ hàng" trước "Đăng xuất"
            menuItems = new String[]{fullname + " (Chi tiết)","Trang chủ", "Menu", "Lịch sử đơn hàng", "Giỏ hàng", "Đăng xuất"};
        } else {
            // Thêm "Giỏ hàng" trước "Đăng nhập"
            menuItems = new String[]{"Trang chủ", "Menu", "Lịch sử đơn hàng", "Giỏ hàng", "Đăng nhập"};
        }
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        ListView menu = findViewById(R.id.menu);

        menu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems));

        menu.setOnItemClickListener((adapterView, view, i, l) -> {
            drawerLayout.closeDrawers();
            if (isLoggedIn) {
                switch (i) {
                    case 0: // Thông tin người dùng
                        startActivity(new Intent(BaseActivity.this, UserInfoActivity.class));
                        break;
                    case 1: // Trang chủ
                        startActivity(new Intent(BaseActivity.this, MainActivity.class));
                        break;
                    case 2: // Menu
                        startActivity(new Intent(BaseActivity.this, MenuActivity.class));
                        break;
                    //case 3: // Lịch sử đơn hàng
                        //startActivity(new Intent(BaseActivity.this, OrderHistoryActivity.class));
                        //break;
                    case 4: // Giỏ hàng
                        startActivity(new Intent(BaseActivity.this, CartActivity.class));
                        break;
                    case 5: // Đăng xuất
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        break;
                }
            } else {
                switch (i) {
                    case 0: // Trang chủ
                        startActivity(new Intent(BaseActivity.this, MainActivity.class));
                        break;
                    case 1: // Menu
                        startActivity(new Intent(BaseActivity.this, MenuActivity.class));
                        break;
                    //case 2: // Lịch sử đơn hàng
                        //startActivity(new Intent(BaseActivity.this, OrderHistoryActivity.class));
                        //break;
                    case 3: // Giỏ hàng
                        startActivity(new Intent(BaseActivity.this, CartActivity.class));
                        break;
                    case 4: // Đăng nhập
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMenu();
    }
}