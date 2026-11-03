package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> cartList;
    private CartActionListener listener;

    public interface CartActionListener {
        void onQuantityChanged(int cartId, int newQuantity);
        void onDelete(int cartId);
    }

    public CartAdapter(List<Cart> cartList, CartActionListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    public void updateData(List<Cart> newList) {
        this.cartList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        holder.ivFood.setImageResource(cart.getFoodImage());
        holder.tvName.setText(cart.getFoodName());
        holder.tvPrice.setText(String.format("¥%.2f", cart.getFoodPrice()));
        holder.tvQuantity.setText(String.valueOf(cart.getQuantity()));

        holder.btnMinus.setOnClickListener(v -> {
            int newQty = cart.getQuantity() - 1;
            if (listener != null) {
                listener.onQuantityChanged(cart.getId(), newQty);
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = cart.getQuantity() + 1;
            if (listener != null) {
                listener.onQuantityChanged(cart.getId(), newQty);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(cart.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvName;
        TextView tvPrice;
        TextView tvQuantity;
        ImageButton btnMinus;
        ImageButton btnPlus;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.iv_food);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvPrice = itemView.findViewById(R.id.tv_food_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}