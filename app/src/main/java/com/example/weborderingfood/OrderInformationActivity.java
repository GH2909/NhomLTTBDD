package com.example.weborderingfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    private TextView tvTotalPrice, tvShippingFee, tvFinalPrice;
    private Button btnDatHang;
    private RecyclerView recyclerViewOrderItems;
    private OrderSummaryAdapter orderSummaryAdapter;

    // Khai báo các Spinner mới cho Phường/Xã và Khu vực
    private Spinner spnPhuongXa, spnKhuVucGiao;

    // Biến để lưu trữ danh sách món ăn và ID người dùng
    private List<FoodItem> cartItems;
    private int userId;

    // Phí vận chuyển mặc định
    private static final double SHIPPING_FEE = 15000;

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
        btnDatHang = findViewById(R.id.btnDatHang);
        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvFinalPrice = findViewById(R.id.tvFinalPrice);

        // Khởi tạo các Spinner mới
        spnPhuongXa = findViewById(R.id.spnPhuongXa);
        spnKhuVucGiao = findViewById(R.id.spnKhuVucGiao);

        // Đổ dữ liệu tạm vào các Spinner
        setupSpinners();

        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));

        // BƯỚC 1: NHẬN DỮ LIỆU TỪ INTENT
        Intent intent = getIntent();
        if (intent.hasExtra("cartItems")) {
            cartItems = (List<FoodItem>) intent.getSerializableExtra("cartItems");
        }

        // BƯỚC 2: HIỂN THỊ TÓM TẮT ĐƠN HÀNG VÀ TỔNG TIỀN
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

    /**
     * Phương thức này thiết lập dữ liệu cho các Spinner.
     * Tạm thời dùng dữ liệu giả. Bạn nên thay thế bằng API call để lấy dữ liệu thực.
     */
    private void setupSpinners() {
        // Dữ liệu cho Phường/Xã dựa trên danh sách bạn cung cấp
        String[] wards = {"-- Chọn phường --", "Phường Bình Thạnh", "Phường Tân Sơn Nhất", "Phường Cầu Kiệu", "Phường An Nhơn", "Phường Hạnh Thông", "Phường Sài Gòn", "Phường Minh Phụng", "Phường Tân Sơn Hòa", "Phường Phú Nhuận", "Phường An Hội Đông"};
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wards);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPhuongXa.setAdapter(wardAdapter);

        // Dữ liệu giả cho Khu vực giao
        String[] regions = {"Thành phố Hồ Chí Minh"};
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKhuVucGiao.setAdapter(regionAdapter);
    }

    private void updateTotalPrice(List<FoodItem> items) {
        double subtotal = 0;
        for (FoodItem item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        // Tổng tiền cuối cùng = Tổng tiền hàng + Phí vận chuyển
        double finalPrice = subtotal + SHIPPING_FEE;

        DecimalFormat formatter = new DecimalFormat("#,### đ");

        // Hiển thị tổng tiền hàng
        tvTotalPrice.setText(formatter.format(subtotal));

        // Hiển thị phí vận chuyển
        tvShippingFee.setText(formatter.format(SHIPPING_FEE));

        // Hiển thị tổng tiền cuối cùng
        tvFinalPrice.setText(formatter.format(finalPrice));
    }

    private void createOrder() {
        String customerName = edtHoTen.getText().toString().trim();
        String phoneNumber = edtSoDienThoai.getText().toString().trim();
        String shippingAddress = edtDiaChi.getText().toString().trim();
        String orderNote = edtGhiChu.getText().toString().trim();

        // Lấy giá trị đã chọn từ Spinner
        String ward = "";
        if (spnPhuongXa.getSelectedItem() != null) {
            ward = spnPhuongXa.getSelectedItem().toString();
        }

        String region = "";
        if (spnKhuVucGiao.getSelectedItem() != null) {
            region = spnKhuVucGiao.getSelectedItem().toString();
        }

        if (customerName.isEmpty() || phoneNumber.isEmpty() || shippingAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm kiểm tra cho Spinner
        if (ward.equals("-- Chọn phường --") || ward.isEmpty() || region.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Phường/Xã và Khu vực", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống, không thể đặt hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // BƯỚC 4: TẠO ĐỐI TƯỢNG ORDERREQUEST VÀ GỬI API
        double subtotal = 0;
        for (FoodItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        // Tổng tiền cuối cùng để gửi lên API
        double totalAmount = subtotal + SHIPPING_FEE;

        String paymentMethod = "Tiền mặt";
        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedId == R.id.radioChuyenKhoan) {
            paymentMethod = "Chuyển khoản";
        }

        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Tạo OrderRequest với userId và các trường mới
        OrderRequest orderRequest = new OrderRequest(
                userId, // Sử dụng biến userId đã lấy từ SharedPreferences
                customerName,
                phoneNumber,
                shippingAddress,
                ward, // Thêm phường/xã
                region, // Thêm khu vực
                orderNote,
                cartItems,
                totalAmount,
                paymentMethod,
                orderDate
        );
        // Log toàn bộ JSON payload để kiểm tra trước khi gửi
        // Sử dụng Gson để chuyển đối tượng thành chuỗi JSON và in ra log
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String jsonPayload = gson.toJson(orderRequest);
        Log.d("OrderInfoActivity", "JSON Payload gửi đi: " + jsonPayload);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<OrderResponse> call = apiService.createOrder(orderRequest);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                // Thêm log để kiểm tra phản hồi từ máy chủ
                Log.d("OrderInfoActivity", "Mã phản hồi từ server: " + response.code());
                Log.d("OrderInfoActivity", "Thông điệp phản hồi: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    // Kiểm tra trạng thái "success"
                    Log.d("OrderInfoActivity", "Trạng thái phản hồi: " + response.body().getStatus());

                    if ("success".equals(response.body().getStatus())) {
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
                        // Trường hợp phản hồi thành công nhưng trạng thái không phải là "success"
                        String message = response.body().getMessage();
                        Toast.makeText(OrderInformationActivity.this, "Lỗi đặt hàng: " + message, Toast.LENGTH_LONG).show();
                        Log.e("OrderInfoActivity", "Lỗi đặt hàng: " + message);
                    }
                } else {
                    String error = "Lỗi khi đặt hàng";
                    if (response.errorBody() != null) {
                        try {
                            error = response.errorBody().string();
                            Log.e("OrderInfoActivity", "Lỗi server: " + error);
                            // Kiểm tra xem lỗi có phải do thiếu user_id không
                            if (error.contains("L\u1ed7i: Ch\u01b0a \u0111\u0103ng nh\u1eadp!")) {
                                error = "Lỗi: Vui lòng đăng nhập lại.";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("OrderInfoActivity", "Không có body lỗi nào.");
                    }
                    Toast.makeText(OrderInformationActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log lỗi kết nối mạng
                Log.e("OrderInfoActivity", "Lỗi kết nối mạng: " + t.getMessage());
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
