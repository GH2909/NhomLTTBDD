package com.example.weborderingfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weborderingfood.R;
import com.example.weborderingfood.OrderItem;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter cho RecyclerView hiển thị danh sách các món ăn trong chi tiết đơn hàng.
 * Adapter này chịu trách nhiệm liên kết dữ liệu OrderItem với các View trong layout item.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderItem> orderItems;

    public OrderDetailAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_row, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvQuantity.setText("x" + item.getQuantity());

        // Định dạng giá tiền
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        formatter.setMinimumFractionDigits(0);
        String formattedPrice = formatter.format(item.getPrice()).replace(" ₫", " đ");
        holder.tvPrice.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    /**
     * ViewHolder để lưu trữ các View của mỗi item trong RecyclerView.
     */
    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvQuantity;
        TextView tvPrice;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
