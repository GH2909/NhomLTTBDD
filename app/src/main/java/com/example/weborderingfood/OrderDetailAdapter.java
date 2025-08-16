//package com.example.weborderingfood;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.weborderingfood.R;
//import com.example.weborderingfood.models.OrderItem;
//import java.util.List;
//
///**
// * Adapter for displaying a list of order items in a RecyclerView.
// */
//public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
//
//    private List<OrderItem> orderItems;
//
//    public OrderDetailAdapter(List<OrderItem> orderItems) {
//        this.orderItems = orderItems;
//    }
//
//    @NonNull
//    @Override
//    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.order_item_row, parent, false);
//        return new OrderDetailViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
//        OrderItem item = orderItems.get(position);
//        holder.tvProductName.setText(item.getProductName());
//        // Hiển thị số lượng
//        holder.tvQuantity.setText(String.format("x%d", item.getQuantity()));
//        // Hiển thị giá sản phẩm, định dạng với đơn vị tiền tệ
//        holder.tvPrice.setText(String.format("%,.0fđ", item.getPrice()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderItems.size();
//    }
//
//    /**
//     * ViewHolder class to hold references to the views for each list item.
//     */
//    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
//        public TextView tvProductName;
//        public TextView tvQuantity;
//        public TextView tvPrice;
//
//        public OrderDetailViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvProductName = itemView.findViewById(R.id.tvProductName);
//            tvQuantity = itemView.findViewById(R.id.tvQuantity);
//            tvPrice = itemView.findViewById(R.id.tvPrice);
//        }
//    }
//}
