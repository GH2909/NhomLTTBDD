package com.example.weborderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private final Context context;
    private final List<FoodItem> orderItems;

    public OrderSummaryAdapter(Context context, List<FoodItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_row, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        FoodItem item = orderItems.get(position);
        holder.tvProductName.setText(item.getName());
        holder.tvQuantity.setText("x" + item.getQuantity());
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        holder.tvPrice.setText(formatter.format(item.getPrice() * item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public List<FoodItem> getOrderItems() {
        return orderItems;
    }

    public static class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;

        public OrderSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}