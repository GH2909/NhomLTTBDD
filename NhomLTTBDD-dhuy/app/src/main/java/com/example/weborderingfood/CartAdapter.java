package com.example.weborderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<FoodItem> cartItems;
    private Runnable updateTotalPriceCallback;

    public CartAdapter(Context context, List<FoodItem> cartItems, Runnable updateTotalPriceCallback) {
        this.context = context;
        this.cartItems = cartItems;
        this.updateTotalPriceCallback = updateTotalPriceCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        FoodItem item = cartItems.get(position);

        holder.tvFoodName.setText(item.getName());
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        holder.tvFoodPrice.setText(formatter.format(item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        String fullImageUrl = Constants.BASE_URL + item.getImageUrl();
        Glide.with(context).load(fullImageUrl).into(holder.imgFood);

        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            updateQuantityOnServer(item, newQuantity, position);
        });

        holder.btnMinus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity >= 1) {
                updateQuantityOnServer(item, newQuantity, position);
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            removeItemFromServer(item, position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public List<FoodItem> getCartItems() {
        return cartItems;
    }

    private void updateQuantityOnServer(FoodItem item, int newQuantity, int position) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        CartRequest request = new CartRequest("updateQuantity", item.getId(), newQuantity);
        Call<CartApiResponse> call = apiService.updateItemQuantity(request);

        call.enqueue(new Callback<CartApiResponse>() {
            @Override
            public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    item.setQuantity(newQuantity);
                    notifyItemChanged(position);
                    updateTotalPriceCallback.run();
                } else {
                    Toast.makeText(context, "Lỗi khi cập nhật số lượng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CartApiResponse> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeItemFromServer(FoodItem item, int position) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        CartRequest request = new CartRequest("removeItem", item.getId(), 0);
        Call<CartApiResponse> call = apiService.removeItemFromCart(request);

        call.enqueue(new Callback<CartApiResponse>() {
            @Override
            public void onResponse(Call<CartApiResponse> call, Response<CartApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                    updateTotalPriceCallback.run();
                } else {
                    Toast.makeText(context, "Lỗi khi xóa món", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CartApiResponse> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateData(List<FoodItem> newCartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newCartItems);
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName, tvFoodPrice, tvQuantity;
        Button btnPlus, btnMinus;
        ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}