package com.example.weborderingfood;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<FoodItem> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_menu);

        // Lấy tên danh mục từ intent
        String category = getIntent().getStringExtra("category");
        TextView tvTitle = findViewById(R.id.tvCategoryTitle);
        tvTitle.setText("Danh mục: " + category);

        // Cấu hình RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gán dữ liệu mẫu theo danh mục
        foodList = getSampleData(category);
        adapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(adapter);
    }

    private List<FoodItem> getSampleData(String category) {
        List<FoodItem> list = new ArrayList<>();

        switch (category) {
            case "Khuyến mãi":
                list.add(new FoodItem("Gà Popcorn", "Miếng gà nhỏ chiên giòn", 50000, R.drawable.ga_popcorn));
                list.add(new FoodItem("Khoai lắc phô mai", "Khoai tây chiên xóc phô mai", 30000, R.drawable.khoai_lac));
                break;
            case "Món mới":
                list.add(new FoodItem("Trà Sữa", "Trà sữa trân châu", 40000, R.drawable.tra_sua));
                list.add(new FoodItem("Matcha Latte", "Trà sữa matcha", 40000, R.drawable.matcha_latte));
                break;
            case "Combo":
                list.add(new FoodItem("Combo Gia Đình", "Combo cho 4 người", 200000, R.drawable.combo_giadinh));
                list.add(new FoodItem("Combo Tiệc Vui", "Combo tiệc nhóm", 170000, R.drawable.combo_tiec));
                list.add(new FoodItem("Combo Cô Đơn", "Combo cho 1 người", 70000, R.drawable.combo_codon));
                list.add(new FoodItem("Combo Cặp Đôi", "Combo cho 2 người", 120000, R.drawable.combo_capdoi));
                break;
            case "Gà rán":
                list.add(new FoodItem("Gà Rán", "Gà rán giòn tan", 45000, R.drawable.ga2));
                break;
            case "Burger - Cơm - Mỳ Ý":
                list.add(new FoodItem("Burger Bò", "Burger nhân bò", 50000, R.drawable.hamburger));
                list.add(new FoodItem("Mỳ Ý Sốt Bò", "Mỳ Ý truyền thống", 55000, R.drawable.miy_bobam));
                break;
            case "Tráng miệng":
                list.add(new FoodItem("Pepsi", "Pepsi lạnh", 15000, R.drawable.pepsi));
                list.add(new FoodItem("Trà Đào", "Trà đào thơm mát", 35000, R.drawable.tra_dao));
                break;
        }

        return list;
    }
}
