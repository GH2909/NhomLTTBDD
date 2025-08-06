package com.example.weborderingfood;

import static com.example.weborderingfood.Constants.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    TextView textLogup;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        textLogup = findViewById(R.id.textLogup);
        btnLogin = findViewById(R.id.btnLogin);

        textLogup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            login(email, password);
        });
    }


    private void login(String email, String password) {
        new Thread(() -> {
            try {
                URL url =new URL(BASE_URL + "php/auth/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setInstanceFollowRedirects(false); // Quan trọng: để tự xử lý redirect
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                String location = conn.getHeaderField("Location");

                runOnUiThread(() -> {
                    if (responseCode == 301 || responseCode == 302) {
                        if (location == null) {
                            showToast("Không có phản hồi từ server");
                        } else if (location.contains("loginok") || location.contains("index.php")) {
                            showToast("Đăng nhập thành công");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (location.contains("fail")) {
                            showToast("Sai email hoặc mật khẩu");
                        } else if (location.contains("blocked")) {
                            showToast("Tài khoản đã bị khóa");
                        } else if (location.contains("empty")) {
                            showToast("Vui lòng nhập đầy đủ thông tin");
                        } else {
                            showToast("Không xác định: " + location);
                        }
                    } else {
                        showToast("Mã phản hồi không hợp lệ: " + responseCode);
                    }
                });

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showToast("Lỗi kết nối: " + e.getMessage()));
            }
        }).start();
    }

    private void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_LONG).show());
    }
}
