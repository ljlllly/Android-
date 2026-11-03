package com.example.foodorderapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DatabaseHelper;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.OrderAdapter;
import com.example.foodorderapp.model.Order;

import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView rvOrders;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private OrderAdapter adapter;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        rvOrders = view.findViewById(R.id.rv_orders);
        tvEmpty = view.findViewById(R.id.tv_empty);
        dbHelper = DatabaseHelper.getInstance(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("food_order_prefs",
                requireContext().MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void loadOrderData() {
        List<Order> orderList = dbHelper.getOrderList(userId);
        if (orderList.isEmpty()) {
            rvOrders.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new OrderAdapter(orderList);
                rvOrders.setAdapter(adapter);
            } else {
                adapter.updateData(orderList);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrderData();
    }
}