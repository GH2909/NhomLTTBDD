package com.example.weborderingfood;

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
    String[] textMenu = {"Món mới", "Combo", "Gà Rán", "Khuyến mãi","Tráng miệng", "Khác"};
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

        MenuAdapter adapter = new MenuAdapter(this, imagesMenu, textMenu);
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}