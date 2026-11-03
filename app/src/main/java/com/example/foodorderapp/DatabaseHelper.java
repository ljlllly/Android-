package com.example.foodorderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FoodOrder.db";
    private static final int DB_VERSION = 1;

    // 建表 SQL
    private static final String CREATE_USER =
            "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, phone TEXT)";

    private static final String CREATE_FOOD =
            "CREATE TABLE food (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, price REAL NOT NULL, category TEXT, " +
                    "image INTEGER, description TEXT)";

    private static final String CREATE_CART =
            "CREATE TABLE cart (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, food_id INTEGER NOT NULL, quantity INTEGER NOT NULL DEFAULT 1, " +
                    "UNIQUE(user_id, food_id))";

    private static final String CREATE_ORDERS =
            "CREATE TABLE orders (id TEXT PRIMARY KEY, " +
                    "user_id INTEGER NOT NULL, total_price REAL NOT NULL, " +
                    "status TEXT NOT NULL DEFAULT '待支付', create_time TEXT NOT NULL)";

    private static volatile DatabaseHelper instance;
    private final Context context;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_FOOD);
        db.execSQL(CREATE_CART);
        db.execSQL(CREATE_ORDERS);
        insertTestFoods(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS food");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    // ==================== Food 相关 ====================

    private void insertTestFoods(SQLiteDatabase db) {
        Object[][] foods = {
                {1, "红烧肉", 38.00, "热菜", android.R.drawable.ic_menu_gallery, "精选五花肉，慢火炖制，肥而不腻，入口即化。"},
                {2, "宫保鸡丁", 28.00, "热菜", android.R.drawable.ic_menu_gallery, "鸡丁嫩滑，花生香脆，经典川味，麻辣鲜香。"},
                {3, "番茄炒蛋", 18.00, "热菜", android.R.drawable.ic_menu_gallery, "家常美味，酸甜可口，营养丰富，老少皆宜。"},
                {4, "糖醋里脊", 32.00, "热菜", android.R.drawable.ic_menu_gallery, "外酥里嫩，酸甜适口，金黄诱人，回味无穷。"},
                {5, "麻婆豆腐", 22.00, "热菜", android.R.drawable.ic_menu_gallery, "麻辣嫩滑，豆腐入口即化，正宗川味经典。"},
                {6, "鱼香肉丝", 26.00, "热菜", android.R.drawable.ic_menu_gallery, "酸甜微辣，肉丝嫩滑，下饭神器。"},
                {7, "可乐鸡翅", 35.00, "小吃", android.R.drawable.ic_menu_gallery, "色泽红亮，甜咸适中，鸡翅鲜嫩多汁。"},
                {8, "凉拌黄瓜", 12.00, "凉菜", android.R.drawable.ic_menu_gallery, "清爽脆口，蒜香四溢，开胃解腻。"},
                {9, "皮蛋豆腐", 15.00, "凉菜", android.R.drawable.ic_menu_gallery, "嫩滑豆腐搭配香醇皮蛋，清凉爽口。"},
                {10, "酸辣土豆丝", 16.00, "热菜", android.R.drawable.ic_menu_gallery, "土豆丝爽脆，酸辣开胃，家常经典。"},
        };

        ContentValues values = new ContentValues();
        for (Object[] f : foods) {
            values.clear();
            values.put("id", (Integer) f[0]);
            values.put("name", (String) f[1]);
            values.put("price", (Double) f[2]);
            values.put("category", (String) f[3]);
            values.put("image", (Integer) f[4]);
            values.put("description", (String) f[5]);
            db.insert("food", null, values);
        }
    }

    /**
     * 获取所有菜品
     */
    public List<Food> getAllFoods() {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM food ORDER BY id", null);
        while (c.moveToNext()) {
            Food food = new Food(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("price")),
                    c.getString(c.getColumnIndexOrThrow("category")),
                    c.getInt(c.getColumnIndexOrThrow("image")),
                    c.getString(c.getColumnIndexOrThrow("description"))
            );
            list.add(food);
        }
        c.close();
        return list;
    }

    public Food getFoodById(int foodId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM food WHERE id = ?", new String[]{String.valueOf(foodId)});
        Food food = null;
        if (c.moveToFirst()) {
            food = new Food(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("price")),
                    c.getString(c.getColumnIndexOrThrow("category")),
                    c.getInt(c.getColumnIndexOrThrow("image")),
                    c.getString(c.getColumnIndexOrThrow("description"))
            );
        }
        c.close();
        return food;
    }

    // ==================== User 相关 ====================

    /**
     * 注册用户
     * @return 成功返回 userId，失败返回 -1
     */
    public long registerUser(String username, String password, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("phone", phone);
        long id = db.insert("user", null, values);
        return id;
    }

    /**
     * 登录验证
     * @return 成功返回 User 对象，失败返回 null
     */
    public User login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?",
                new String[]{username, password});
        User user = null;
        if (c.moveToFirst()) {
            user = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("username")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("phone"))
            );
        }
        c.close();
        return user;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user WHERE id = ?",
                new String[]{String.valueOf(userId)});
        User user = null;
        if (c.moveToFirst()) {
            user = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("username")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("phone"))
            );
        }
        c.close();
        return user;
    }

    // ==================== Cart 相关 ====================

    /**
     * 添加到购物车，同一用户+同一菜品则数量累加
     */
    public void addToCart(int userId, int foodId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id, quantity FROM cart WHERE user_id = ? AND food_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(foodId)});
        if (c.moveToFirst()) {
            int existQty = c.getInt(c.getColumnIndexOrThrow("quantity"));
            int cartId = c.getInt(c.getColumnIndexOrThrow("id"));
            ContentValues values = new ContentValues();
            values.put("quantity", existQty + quantity);
            db.update("cart", values, "id = ?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("food_id", foodId);
            values.put("quantity", quantity);
            db.insert("cart", null, values);
        }
        c.close();
    }

    /**
     * 获取某用户购物车列表（带菜品信息）
     */
    public List<Cart> getCartList(int userId) {
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT c.id, c.user_id, c.food_id, c.quantity, f.name, f.price, f.image " +
                "FROM cart c INNER JOIN food f ON c.food_id = f.id " +
                "WHERE c.user_id = ? ORDER BY c.id";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        while (c.moveToNext()) {
            Cart cart = new Cart(
                    c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3)
            );
            cart.setFoodName(c.getString(4));
            cart.setFoodPrice(c.getDouble(5));
            cart.setFoodImage(c.getInt(6));
            list.add(cart);
        }
        c.close();
        return list;
    }

    /**
     * 更新购物车菜品数量
     */
    public void updateCartQuantity(int cartId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        if (quantity <= 0) {
            db.delete("cart", "id = ?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put("quantity", quantity);
            db.update("cart", values, "id = ?", new String[]{String.valueOf(cartId)});
        }
    }

    /**
     * 删除购物车中的商品
     */
    public void deleteCartItem(int cartId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "id = ?", new String[]{String.valueOf(cartId)});
    }

    /**
     * 清空用户购物车
     */
    public void clearCart(int userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "user_id = ?", new String[]{String.valueOf(userId)});
    }

    /**
     * 计算购物车总价
     */
    public double getCartTotal(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT SUM(c.quantity * f.price) FROM cart c " +
                "INNER JOIN food f ON c.food_id = f.id WHERE c.user_id = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        double total = 0;
        if (c.moveToFirst() && !c.isNull(0)) {
            total = c.getDouble(0);
        }
        c.close();
        return total;
    }

    // ==================== Order 相关 ====================

    /**
     * 提交订单：生成一条订单记录，清空购物车
     */
    public void submitOrder(int userId, double totalPrice) {
        SQLiteDatabase db = getWritableDatabase();
        String orderId = generateOrderId();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        ContentValues values = new ContentValues();
        values.put("id", orderId);
        values.put("user_id", userId);
        values.put("total_price", totalPrice);
        values.put("status", "待支付");
        values.put("create_time", createTime);
        db.insert("orders", null, values);
    }

    /**
     * 获取用户订单列表
     */
    public List<Order> getOrderList(int userId) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM orders WHERE user_id = ? ORDER BY create_time DESC",
                new String[]{String.valueOf(userId)});
        while (c.moveToNext()) {
            Order order = new Order(
                    c.getString(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")),
                    c.getDouble(c.getColumnIndexOrThrow("total_price")),
                    c.getString(c.getColumnIndexOrThrow("status")),
                    c.getString(c.getColumnIndexOrThrow("create_time"))
            );
            list.add(order);
        }
        c.close();
        return list;
    }

    private String generateOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        return "OD" + sdf.format(new Date());
    }
}