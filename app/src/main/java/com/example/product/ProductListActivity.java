package com.example.product;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.product.dao.ProductDao;
import com.example.product.databinding.ActivityListProductBinding;
import com.example.product.entity.Product;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    ActivityListProductBinding binding;
    AppDatabase db;
    ProductDao dao;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Product List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);
        dao = db.productDao();

        if (dao.getAll().isEmpty()) {
            dao.insert(new Product("Laptop", 999.99));
            dao.insert(new Product("Keyboard", 49.99));
            dao.insert(new Product("Mouse", 29.99));
            dao.insert(new Product("Monitor", 199.99));
            dao.insert(new Product("Headphones", 89.99));
            dao.insert(new Product("Smartphone", 699.99));
            dao.insert(new Product("USB Cable", 9.99));
        }

        List<Product> products = dao.getAll();
        adapter = new ProductAdapter(products, dao, this::refreshList);
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewProducts.setAdapter(adapter);
    }

    private void refreshList() {
        adapter.refreshData(dao.getAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_product) {
            showAddDialog();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_form_product, null);
        EditText etName = dialogView.findViewById(R.id.etAddName);
        EditText etPrice = dialogView.findViewById(R.id.etAddPrice);

        new AlertDialog.Builder(this)
                .setTitle("Add Product")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String priceText = etPrice.getText().toString().trim();
                    if (!name.isEmpty() && !priceText.isEmpty()) {
                        double price = Double.parseDouble(priceText);
                        dao.insert(new Product(name, price));
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
