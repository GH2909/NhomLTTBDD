package com.example.weborderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat; // Import thư viện DecimalFormat
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<FoodItem> foodList;

    public FoodAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem food = foodList.get(position);

        // Sửa tên biến để khớp với ID trong XML
        holder.tvFoodName.setText(food.getName());
        holder.tvFoodDescription.setText(food.getDescription());

        // Định dạng giá tiền để hiển thị chuyên nghiệp hơn
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        String formattedPrice = formatter.format(food.getPrice());
        holder.tvFoodPrice.setText(formattedPrice);

        // Sử dụng Glide để tải ảnh từ URL đầy đủ
        if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
            // Nối BASE_URL với đường dẫn ảnh
            String fullImageUrl = Constants.BASE_URL + food.getImageUrl();
            Glide.with(context)
                    .load(fullImageUrl)
                    .into(holder.imgFood);
        } else {
            // Nếu không có ảnh, có thể đặt ảnh placeholder
            holder.imgFood.setImageResource(R.drawable.garan); // Thay thế bằng một ảnh mặc định
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        // Sửa tên biến để khớp với các ID trong XML
        TextView tvFoodName, tvFoodDescription, tvFoodPrice;
        Button btnAdd;

        public FoodViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            // Ánh xạ các biến với các ID chính xác
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}