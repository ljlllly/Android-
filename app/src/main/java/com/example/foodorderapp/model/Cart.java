package com.example.foodorderapp.model;

public class Cart {
    private int id;
    private int userId;
    private int foodId;
    private int quantity;
    // 以下字段用于列表展示，不存数据库
    private String foodName;
    private double foodPrice;
    private int foodImage;

    public Cart() {}

    public Cart(int id, int userId, int foodId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public double getFoodPrice() { return foodPrice; }
    public void setFoodPrice(double foodPrice) { this.foodPrice = foodPrice; }
    public int getFoodImage() { return foodImage; }
    public void setFoodImage(int foodImage) { this.foodImage = foodImage; }
}