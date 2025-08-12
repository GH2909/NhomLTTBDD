package com.example.weborderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity {

    //    Slideshow
    int[] imagesSlideshow = {R.drawable.home1, R.drawable.home2, R.drawable.home3};
    int currentIndex = 0;
    Handler handler = new Handler();
    Runnable runnable;
    //    Menu
    int[] imagesMenu = {R.drawable.monmoi, R.drawable.combo, R.drawable.garan, R.drawable.muc_ran, R.drawable.trangmieng,R.drawable.other};
    String[] textMenu = {"Món mới", "Combo", "Gà rán", "Khuyến mãi", "Tráng miệng", "Khác"};
    String[] categoryNames = {"Món mới", "Combo", "Gà rán", "Khuyến mãi", "Tráng miệng", "Burger - Cơm - Mì ý"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_main);
//        Slideshow
        ImageView imgThumb = findViewById(R.id.imgThumb);
        runnable = new Runnable() {
            @Override
            public void run() {
                imgThumb.setImageResource(imagesSlideshow[currentIndex]);
                currentIndex = (currentIndex + 1) % imagesSlideshow.length;
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(runnable);
//        Menu
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setNestedScrollingEnabled(false);

        // Tạo adapter với listener
        MenuAdapter adapter = new MenuAdapter(this, imagesMenu, textMenu, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String category) {
                // Sửa logic ở đây để xử lý trường hợp "Khác"
                String categoryToSend = category;
                if (category.equals("Khác")) {
                    categoryToSend = "Burger - Cơm - Mì ý";
                }
                openMenuActivity(categoryToSend);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    // Phương thức mới để mở MenuActivity
    private void openMenuActivity(String category) {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}