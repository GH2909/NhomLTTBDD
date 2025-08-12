package com.example.weborderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private int[] imagesMenu;
    private String[] textMenu;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String category);
    }

    public MenuAdapter(Context context, int[] imagesMenu, String[] textMenu, OnItemClickListener listener) {
        this.context = context;
        this.imagesMenu = imagesMenu;
        this.textMenu = textMenu;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_section, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        // Đây là chữ ký phương thức đúng.
        // Lỗi không phải ở đây.
        holder.imgMenu.setImageResource(imagesMenu[position]);
        holder.tvTitle.setText(textMenu[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(textMenu[position]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return textMenu.length;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenu;
        TextView tvTitle; // Tên biến đã được đổi để khớp với XML

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            tvTitle = itemView.findViewById(R.id.txtTitle); // ID đã được đổi để khớp với XML
        }
    }
}