package com.example.weborderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<FoodItem> foodList;
    private CartUpdateListener updateListener;

    // SỬA CONSTRUCTOR ĐỂ NHẬN VÀ LƯU LISTENER
    public FoodAdapter(Context context, List<FoodItem> foodList, CartUpdateListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.updateListener = listener;
    }

    // CONSTRUCTOR CŨ NẾU CÒN DÙNG THÌ VẪN GIỮ
    public FoodAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    // THÊM: Phương thức để cập nhật dữ liệu cho Adapter
    public void updateData(List<FoodItem> newFoodList) {
        this.foodList.clear();
        this.foodList.addAll(newFoodList);
        notifyDataSetChanged();
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

        holder.tvFoodName.setText(food.getName());
        holder.tvFoodDescription.setText(food.getDescription());

        DecimalFormat formatter = new DecimalFormat("#,### đ");
        String formattedPrice = formatter.format(food.getPrice());
        holder.tvFoodPrice.setText(formattedPrice);

        if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
            String fullImageUrl = Constants.BASE_URL + food.getImageUrl();
            Glide.with(context)
                    .load(fullImageUrl)
                    .into(holder.imgFood);
        } else {
            holder.imgFood.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.btnAdd.setOnClickListener(v -> {
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            CartRequest request = new CartRequest("addItem", food.getId(), 1);
            Call<CartApiResponse> call = apiService.addItemToCart(request);

            call.enqueue(new Callback<CartApiResponse>() {
                @Override
                public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                        Toast.makeText(context, "Đã thêm " + food.getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        // GỌI LISTENER ĐỂ THÔNG BÁO CẬP NHẬT GIỎ HÀNG
                        if (updateListener != null) {
                            updateListener.onCartUpdated();
                        }
                    } else {
                        Toast.makeText(context, "Lỗi khi thêm món!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CartApiResponse> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName, tvFoodDescription, tvFoodPrice;
        Button btnAdd;

        public FoodViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}