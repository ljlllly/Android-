package com.example.foodorderapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DatabaseHelper;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CartAdapter;
import com.example.foodorderapp.model.Cart;

import java.text.DecimalFormat;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private TextView tvTotal;
    private TextView tvEmpty;
    private Button btnSubmit;
    private View bottomBar;
    private DatabaseHelper dbHelper;
    private CartAdapter adapter;
    private int userId;
    private DecimalFormat df = new DecimalFormat("0.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCart = view.findViewById(R.id.rv_cart);
        tvTotal = view.findViewById(R.id.tv_total);
        tvEmpty = view.findViewById(R.id.tv_empty);
        btnSubmit = view.findViewById(R.id.btn_submit);
        bottomBar = view.findViewById(R.id.bottom_bar);
        dbHelper = DatabaseHelper.getInstance(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("food_order_prefs",
                requireContext().MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSubmit.setOnClickListener(v -> submitOrder());

        return view;
    }

    private void loadCartData() {
        List<Cart> cartList = dbHelper.getCartList(userId);
        double total = dbHelper.getCartTotal(userId);

        if (cartList.isEmpty()) {
            rvCart.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvCart.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new CartAdapter(cartList, new CartAdapter.CartActionListener() {
                    @Override
                    public void onQuantityChanged(int cartId, int newQuantity) {
                        dbHelper.updateCartQuantity(cartId, newQuantity);
                        loadCartData();
                    }

                    @Override
                    public void onDelete(int cartId) {
                        dbHelper.deleteCartItem(cartId);
                        loadCartData();
                    }
                });
                rvCart.setAdapter(adapter);
            } else {
                adapter.updateData(cartList);
            }

            tvTotal.setText(String.format("合计：¥%s", df.format(total)));
        }
    }

    private void submitOrder() {
        List<Cart> cartList = dbHelper.getCartList(userId);
        if (cartList.isEmpty()) {
            Toast.makeText(getContext(), "购物车为空", Toast.LENGTH_SHORT).show();
            return;
        }
        double total = dbHelper.getCartTotal(userId);
        dbHelper.submitOrder(userId, total);
        dbHelper.clearCart(userId);
        Toast.makeText(getContext(), "下单成功！", Toast.LENGTH_SHORT).show();
        loadCartData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartData();
    }
}