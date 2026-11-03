package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodorderapp.DatabaseHelper;
import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Food;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageView ivFood;
    private TextView tvFoodName;
    private TextView tvFoodPrice;
    private TextView tvFoodDesc;
    private TextView tvTagCategory;
    private TextView tvQuantity;
    private ImageButton btnMinus;
    private ImageButton btnPlus;
    private Button btnAddCart;
    private Toolbar toolbar;

    private DatabaseHelper dbHelper;
    private Food food;
    private int quantity = 1;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        dbHelper = DatabaseHelper.getInstance(this);

        int foodId = getIntent().getIntExtra("food_id", -1);
        if (foodId == -1) {
            finish();
            return;
        }

        // 从 MainActivity 获取 userId（通过 SharedPreferences）
        userId = getSharedPreferences("food_order_prefs", MODE_PRIVATE).getInt("user_id", -1);

        food = dbHelper.getFoodById(foodId);
        if (food == null) {
            finish();
            return;
        }

        initViews();
        setupToolbar();
        bindData();
        initListeners();
    }

    private void initViews() {
        ivFood = findViewById(R.id.iv_food);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvFoodPrice = findViewById(R.id.tv_food_price);
        tvFoodDesc = findViewById(R.id.tv_food_desc);
        tvTagCategory = findViewById(R.id.tv_tag_category);
        tvQuantity = findViewById(R.id.tv_quantity);
        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);
        btnAddCart = findViewById(R.id.btn_add_cart);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        toolbar.setTitle(food.getName());
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void bindData() {
        ivFood.setImageResource(food.getImage());
        tvFoodName.setText(food.getName());
        tvFoodPrice.setText(String.format("¥%.2f", food.getPrice()));
        tvFoodDesc.setText(food.getDescription());
        tvTagCategory.setText(food.getCategory());
    }

    private void initListeners() {
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnAddCart.setOnClickListener(v -> {
            dbHelper.addToCart(userId, food.getId(), quantity);
            Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}