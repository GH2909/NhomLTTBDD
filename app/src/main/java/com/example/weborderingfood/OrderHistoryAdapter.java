package com.example.weborderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weborderingfood.OrderHistoryItem;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private Context context;
    private List<OrderHistoryItem> orderList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String orderId);
    }

    public OrderHistoryAdapter(Context context, List<OrderHistoryItem> orderList, OnItemClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_item, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistoryItem order = orderList.get(position);
        holder.tvOrderId.setText( "#" + order.getOrderId());
        holder.tvOrderDate.setText( order.getOrderDate());
        holder.tvTotalAmount.setText( order.getTotalAmount() + "đ");
        holder.tvOrderStatus.setText( order.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(order.getOrderId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalAmount, tvOrderStatus, tvOrderDate;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
