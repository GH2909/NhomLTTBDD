package com.example.weborderingfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_user_info);
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String name = prefs.getString("fullname", "Unknown");
        String email = prefs.getString("email", "Unknown");

        TextView txtName = findViewById(R.id.txtFullname);
        TextView txtEmail = findViewById(R.id.txtEmail);

        txtName.setText("KFJOLIer: "+name);
        txtEmail.setText("Email: "+email);
    }
}