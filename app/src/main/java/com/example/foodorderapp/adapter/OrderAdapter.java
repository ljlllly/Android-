package com.example.foodorderapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void updateData(List<Order> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("订单号：" + order.getId());
        holder.tvStatus.setText(order.getStatus());
        holder.tvTime.setText(order.getCreateTime());
        holder.tvTotal.setText(String.format("¥%.2f", order.getTotalPrice()));

        // 根据状态设置不同颜色
        if ("已完成".equals(order.getStatus())) {
            holder.tvStatus.setTextColor(0xFF4CAF50);
        } else {
            holder.tvStatus.setTextColor(0xFFE65100);
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvStatus;
        TextView tvTime;
        TextView tvTotal;

        ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
            tvTime = itemView.findViewById(R.id.tv_order_time);
            tvTotal = itemView.findViewById(R.id.tv_order_total);
        }
    }
}