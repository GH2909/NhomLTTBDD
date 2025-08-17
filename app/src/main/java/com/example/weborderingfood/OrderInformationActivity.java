package com.example.weborderingfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderInformationActivity extends BaseActivity {

    private EditText edtHoTen, edtSoDienThoai, edtDiaChi, edtGhiChu;
    private RadioGroup radioGroupPayment;
    private TextView tvTotalPrice;
    private Button btnDatHang;
    private RecyclerView recyclerViewOrderItems;
    private OrderSummaryAdapter orderSummaryAdapter;

    // Biến để lưu trữ danh sách món ăn và ID người dùng
    private List<FoodItem> cartItems;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_order_information);

        // Khởi tạo các View
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnDatHang = findViewById(R.id.btnDatHang);
        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);

        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));

        // BƯỚC 1: NHẬN DỮ LIỆU TỪ INTENT
        Intent intent = getIntent();
        if (intent.hasExtra("cartItems")) {
            cartItems = (List<FoodItem>) intent.getSerializableExtra("cartItems");
        }

        // BƯỚC 2: HIỂN THỊ TÓM TẮT ĐƠN HÀNG
        if (cartItems != null) {
            orderSummaryAdapter = new OrderSummaryAdapter(this, cartItems);
            recyclerViewOrderItems.setAdapter(orderSummaryAdapter);
            updateTotalPrice(cartItems);
        } else {
            Toast.makeText(this, "Không có dữ liệu giỏ hàng để hiển thị.", Toast.LENGTH_SHORT).show();
            // Có thể kết thúc Activity nếu không có dữ liệu
            finish();
        }

        // BƯỚC 3: LẤY userId TỪ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(LoginActivity.SHARED_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getInt(LoginActivity.KEY_USER_ID, -1); // Giá trị mặc định là -1

        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            // Điều hướng về màn hình đăng nhập
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        // Xử lý sự kiện khi nhấn nút "ĐẶT HÀNG"
        btnDatHang.setOnClickListener(v -> createOrder());
    }

    private void updateTotalPrice(List<FoodItem> items) {
        double total = 0;
        for (FoodItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        tvTotalPrice.setText(formatter.format(total));
    }

    private void createOrder() {
        String customerName = edtHoTen.getText().toString().trim();
        String phoneNumber = edtSoDienThoai.getText().toString().trim();
        String shippingAddress = edtDiaChi.getText().toString().trim();
        String orderNote = edtGhiChu.getText().toString().trim();

        if (customerName.isEmpty() || phoneNumber.isEmpty() || shippingAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống, không thể đặt hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // BƯỚC 4: TẠO ĐỐI TƯỢNG ORDERREQUEST VÀ GỬI API
        double totalAmount = 0;
        for (FoodItem item : cartItems) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        String paymentMethod = "Tiền mặt";
        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedId == R.id.radioChuyenKhoan) {
            paymentMethod = "Chuyển khoản";
        }

        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Tạo OrderRequest với userId
        OrderRequest orderRequest = new OrderRequest(
                userId, // Sử dụng biến userId đã lấy từ SharedPreferences
                customerName,
                phoneNumber,
                shippingAddress,
                orderNote,
                cartItems,
                totalAmount,
                paymentMethod,
                orderDate
        );

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<OrderResponse> call = apiService.createOrder(orderRequest);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Toast.makeText(OrderInformationActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                    // Xóa giỏ hàng trên server trước khi chuyển màn hình
                    clearCart();

                    // Chuyển sang màn hình chi tiết đơn hàng
                    Intent intent = new Intent(OrderInformationActivity.this, OrderDetailActivity.class);
                    // Truyền orderId để màn hình chi tiết có thể lấy dữ liệu
                    intent.putExtra("order_id", response.body().getOrderId());
                    startActivity(intent);
                    finish(); // Kết thúc màn hình hiện tại
                } else {
                    String error = "Lỗi khi đặt hàng";
                    if (response.errorBody() != null) {
                        try {
                            error = response.errorBody().string();
                            // Kiểm tra xem lỗi có phải do thiếu user_id không
                            if (error.contains("L\u1ed7i: Ch\u01b0a \u0111\u0103ng nh\u1eadp!")) {
                                error = "Lỗi: Vui lòng đăng nhập lại.";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(OrderInformationActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(OrderInformationActivity.this, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Gửi yêu cầu API đến server để xóa giỏ hàng của người dùng.
     * Cần có một endpoint API tương ứng trên server để xử lý tác vụ này.
     */
    private void clearCart() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CartApiResponse> call = apiService.clearCart(); // Giả sử có một API clearCart

        call.enqueue(new Callback<CartApiResponse>() {
            @Override
            public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    Log.d("OrderInformation", "Giỏ hàng đã được xóa thành công trên server.");
                } else {
                    Log.e("OrderInformation", "Lỗi khi xóa giỏ hàng trên server.");
                }
            }

            @Override
            public void onFailure(Call<CartApiResponse> call, Throwable t) {
                Log.e("OrderInformation", "Lỗi kết nối mạng khi xóa giỏ hàng: " + t.getMessage());
            }
        });
    }
}
