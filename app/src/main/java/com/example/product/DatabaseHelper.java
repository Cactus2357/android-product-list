package com.example.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.product.models.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "products.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "product";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL)"
        );

        ContentValues cv = new ContentValues();

        cv.put("name", "Laptop");
        cv.put("price", 999.99);
        db.insert(TABLE_NAME, null, cv);

        cv.clear();
        cv.put("name", "Keyboard");
        cv.put("price", 49.99);
        db.insert(TABLE_NAME, null, cv);

        cv.clear();
        cv.put("name", "Mouse");
        cv.put("price", 25.50);
        db.insert(TABLE_NAME, null, cv);

        cv.clear();
        cv.put("name", "Monitor");
        cv.put("price", 199.00);
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", product.getName());
        cv.put("price", product.getPrice());
        return db.insert(TABLE_NAME, null, cv);
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                list.add(new Product(id, name, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

}
