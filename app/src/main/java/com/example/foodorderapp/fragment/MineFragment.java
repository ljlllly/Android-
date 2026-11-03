package com.example.foodorderapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodorderapp.DatabaseHelper;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.LoginActivity;
import com.example.foodorderapp.model.User;

public class MineFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvPhone;
    private Button btnLogout;
    private DatabaseHelper dbHelper;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        tvUsername = view.findViewById(R.id.tv_username);
        tvPhone = view.findViewById(R.id.tv_phone);
        btnLogout = view.findViewById(R.id.btn_logout);
        dbHelper = DatabaseHelper.getInstance(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("food_order_prefs",
                requireContext().MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        loadUserInfo();

        btnLogout.setOnClickListener(v -> {
            prefs.edit().remove("user_id").apply();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void loadUserInfo() {
        User user = dbHelper.getUserById(userId);
        if (user != null) {
            tvUsername.setText(user.getUsername());
            tvPhone.setText(user.getPhone() != null ? user.getPhone() : "未设置手机号");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }
}