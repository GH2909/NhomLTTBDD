package com.example.weborderingfood;

import static com.example.weborderingfood.Constants.BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

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

    // Định nghĩa các hằng số cho SharedPreferences
    public static final String SHARED_PREFS_NAME = "user";
    public static final String KEY_LOGGED_IN = "logged_in";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_USER_ID = "user_id";

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

    private void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_LONG).show());
    }

    private void login(String email, String password) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "php/auth/login.php");
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
                            // Sau khi đăng nhập thành công, lấy thêm thông tin người dùng
                            getFullnameAndUserIdFromServer(email, () -> {
                                // Chuyển sang trang chính SAU KHI đã có fullname và user_id
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
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

    private void getFullnameAndUserIdFromServer(String email, Runnable onSuccess) {
        new Thread(() -> {
            try {
                // Giả định bạn đã sửa user_info.php để trả về user_id
                URL url = new URL(BASE_URL + "user_info.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String data = "email=" + URLEncoder.encode(email, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                Log.d("DEBUG", "Phản hồi từ server: " + response.toString());

                JSONObject json = new JSONObject(response.toString());

                if (json.getString("status").equals("success")) {
                    JSONObject user = json.getJSONObject("user");
                    String fullname = user.getString("fullname");
                    int userId = user.getInt("id"); // Lấy userId từ phản hồi JSON (sửa tên trường nếu cần)

                    Log.d("DEBUG", "Đã lấy fullname: " + fullname + " và userId: " + userId);

                    runOnUiThread(() -> {
                        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(KEY_FULLNAME, fullname);
                        editor.putInt(KEY_USER_ID, userId); // LƯU USER_ID VÀO SHAREDPREFERENCES
                        editor.putBoolean(KEY_LOGGED_IN, true);
                        editor.apply();

                        Log.d("DEBUG", "Đã lưu thông tin người dùng vào SharedPreferences");
                        onSuccess.run();
                    });
                } else {
                    Log.e("DEBUG", "Lỗi lấy thông tin người dùng: không thành công");
                }
            } catch (Exception e) {
                Log.e("DEBUG", "Lỗi khi gọi user_info.php", e);
            }
        }).start();
    }

}