package com.example.foodorderapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodorderapp.R;
import com.example.foodorderapp.fragment.CartFragment;
import com.example.foodorderapp.fragment.HomeFragment;
import com.example.foodorderapp.fragment.MineFragment;
import com.example.foodorderapp.fragment.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigation;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private OrderFragment orderFragment;
    private MineFragment mineFragment;
    private Fragment activeFragment;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);
        } catch (Exception e) {
            Log.e(TAG, "setContentView failed", e);
            return;
        }

        SharedPreferences prefs = getSharedPreferences("food_order_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        try {
            initFragments();
            initBottomNav();
        } catch (Exception e) {
            Log.e(TAG, "Init failed", e);
        }
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        orderFragment = new OrderFragment();
        mineFragment = new MineFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, cartFragment, "cart")
                .hide(cartFragment)
                .add(R.id.fragment_container, orderFragment, "order")
                .hide(orderFragment)
                .add(R.id.fragment_container, mineFragment, "mine")
                .hide(mineFragment)
                .add(R.id.fragment_container, homeFragment, "home")
                .commit();

        activeFragment = homeFragment;
    }

    private void initBottomNav() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment target = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    target = homeFragment;
                } else if (itemId == R.id.nav_cart) {
                    target = cartFragment;
                } else if (itemId == R.id.nav_orders) {
                    target = orderFragment;
                } else if (itemId == R.id.nav_mine) {
                    target = mineFragment;
                }

                if (target != null && target != activeFragment) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.hide(activeFragment);
                    if (!target.isAdded()) {
                        ft.add(R.id.fragment_container, target).commit();
                    } else {
                        ft.show(target).commit();
                    }
                    activeFragment = target;
                }
                return true;
            }
        });
    }

    public int getUserId() {
        return userId;
    }
}
