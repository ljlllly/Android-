package com.example.foodorderapp.model;

public class Food {
    private int id;
    private String name;
    private double price;
    private String category;
    private int image;
    private String description;

    public Food() {}

    public Food(int id, String name, double price, String category, int image, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getImage() { return image; }
    public void setImage(int image) { this.image = image; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}