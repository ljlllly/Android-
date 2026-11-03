package com.example.foodorderapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.DatabaseHelper;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.FoodDetailActivity;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.model.Food;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvFoods;
    private DatabaseHelper dbHelper;
    private FoodAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvFoods = view.findViewById(R.id.rv_foods);
        dbHelper = DatabaseHelper.getInstance(requireContext());

        rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Food> foodList = dbHelper.getAllFoods();

        adapter = new FoodAdapter(foodList, food -> {
            Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
            intent.putExtra("food_id", food.getId());
            startActivity(intent);
        });

        rvFoods.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次回到首页时刷新数据
        if (adapter != null) {
            adapter.updateData(dbHelper.getAllFoods());
        }
    }
}