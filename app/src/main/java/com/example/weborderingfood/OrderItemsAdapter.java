package com.example.weborderingfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weborderingfood.OrderDetailResponse.OrderItem;

import java.util.List;

/**
 * Adapter for the RecyclerView in OrderDetailActivity.
 * It binds the order item data to the item row layout.
 */
public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItemsList;

    public OrderItemsAdapter(List<OrderItem> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout you provided for each list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_row, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItemsList.get(position);
        if (item != null) {
            // Updated method calls to match the OrderItem class in OrderDetailResponse
            holder.tvProductName.setText(item.getName());
            holder.tvQuantity.setText("x" + item.getQuantity());
            holder.tvPrice.setText(item.getPrice() + "đ"); // Format price as needed
        }
    }

    @Override
    public int getItemCount() {
        return orderItemsList != null ? orderItemsList.size() : 0;
    }

    /**
     * ViewHolder for the order item row.
     * Caches the view components to improve performance.
     */
    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;

        OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
