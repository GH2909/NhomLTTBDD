package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected ImageButton btnMenu;

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        super.setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.main);
        expandableListView = findViewById(R.id.expandableMenu);
        btnMenu = findViewById(R.id.btnMenu);

        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupMenu();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    public void setContentLayout(int layoutResId) {
        FrameLayout frameLayout = findViewById(R.id.contentFrame);
        LayoutInflater.from(this).inflate(layoutResId, frameLayout, true);
    }

    private void setupMenu() {
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        listGroup = new ArrayList<>();
        listItem = new HashMap<>();

        // Nhóm chính
        listGroup.add("Trang chủ");
        listGroup.add("Thực đơn chính");
        listGroup.add("Lịch sử đơn hàng");
        listGroup.add("Đăng nhập");
        listGroup.add("Giỏ hàng của bạn");

        // Mục con của "Thực đơn chính"
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Khuyến mãi");
        menuItems.add("Món mới");
        menuItems.add("Combo");
        menuItems.add("Gà rán");
        menuItems.add("Burger - Cơm - Mỳ Ý");
        menuItems.add("Tráng miệng");

        // Gán danh sách mục con cho từng nhóm (những nhóm không có con thì gán danh sách rỗng)
        listItem.put(listGroup.get(0), new ArrayList<>()); // Trang chủ
        listItem.put(listGroup.get(1), menuItems);         // Thực đơn chính
        listItem.put(listGroup.get(2), new ArrayList<>()); // Lịch sử đơn hàng
        listItem.put(listGroup.get(3), new ArrayList<>()); // Đăng nhập
        listItem.put(listGroup.get(4), new ArrayList<>()); // Giỏ hàng

        ExpandableListAdapter adapter = new ExpandableListAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            String selectedGroup = listGroup.get(groupPosition);
            List<String> children = listItem.get(selectedGroup);

            if (children == null || children.isEmpty()) {
                drawerLayout.closeDrawers();
                switch (groupPosition) {
                    case 0:
                        startActivity(new Intent(this, MainActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(this, OrderHistoryActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(this, LoginActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(this, CartActivity.class));
                        break;
                }
                return true;
            }
            return false; // Nhóm có con thì cho phép mở rộng
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String child = listItem.get(listGroup.get(groupPosition)).get(childPosition);
            drawerLayout.closeDrawers();
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("category", child);
            startActivity(intent);
            return true;
        });
    }
}
