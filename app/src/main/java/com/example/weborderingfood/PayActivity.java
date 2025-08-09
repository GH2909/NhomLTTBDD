package com.example.weborderingfood;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weborderingfood.BaseActivity;

public class PayActivity extends BaseActivity {

    EditText edtHoTen, edtSoDienThoai, edtDiaChi, edtGhiChu;
    Spinner spinnerPhuong, spinnerKhuVuc;
    RadioGroup radioGroupPayment;
    Button btnDatHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_pay); // Gắn layout bạn đã tạo

        // Ánh xạ view từ XML
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        spinnerPhuong = findViewById(R.id.spinnerPhuong);
        spinnerKhuVuc = findViewById(R.id.spinnerKhuVuc);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        btnDatHang = findViewById(R.id.btnDatHang);

        // Gán dữ liệu giả cho Spinner (có thể thay bằng API sau)
        String[] phuongList = {"Phường 1", "Phường 2", "Phường 3"};
        String[] khuVucList = {"Khu A", "Khu B", "Khu C"};

        ArrayAdapter<String> adapterPhuong = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phuongList);
        adapterPhuong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhuong.setAdapter(adapterPhuong);

        ArrayAdapter<String> adapterKhuVuc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khuVucList);
        adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhuVuc.setAdapter(adapterKhuVuc);

        // Sự kiện khi bấm nút "Đặt hàng"
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoTen = edtHoTen.getText().toString().trim();
                String sdt = edtSoDienThoai.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                String ghiChu = edtGhiChu.getText().toString().trim();
                String phuong = spinnerPhuong.getSelectedItem().toString();
                String khuVuc = spinnerKhuVuc.getSelectedItem().toString();

                int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedPaymentId);
                String paymentMethod = (selectedRadio != null) ? selectedRadio.getText().toString() : "Chưa chọn";

                // Thông báo tóm tắt đơn hàng
                String thongTin = "Tên: " + hoTen +
                        "\nSĐT: " + sdt +
                        "\nĐịa chỉ: " + diaChi + ", " + phuong + ", " + khuVuc +
                        "\nGhi chú: " + ghiChu +
                        "\nPhương thức thanh toán: " + paymentMethod;

                Toast.makeText(PayActivity.this, "Đặt hàng thành công!\n" + thongTin, Toast.LENGTH_LONG).show();
            }
        });
    }
}
