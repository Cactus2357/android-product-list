package com.example.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product.databinding.ActivityListProductBinding;
import com.example.product.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    ActivityListProductBinding binding;
    DatabaseHelper dbHelper;
    ProductAdapter adapter;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Product List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        products = dbHelper.getAllProducts();

        adapter = new ProductAdapter(products);
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewProducts.setAdapter(adapter);

//        binding.btnAdd.setOnClickListener(v -> {
//            String name = binding.etName.getText().toString().trim();
//            String priceText = binding.etPrice.getText().toString().trim();
//            if (name.isEmpty() || priceText.isEmpty()) return;
//
//            double price = Double.parseDouble(priceText);
//            dbHelper.insertProduct(new Product(0, name, price));
//            refreshList();
//
//            binding.etName.setText("");
//            binding.etPrice.setText("");
//        });
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
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
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
                        dbHelper.insertProduct(new Product(0, name, price));
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshList() {
        List<Product> updatedList = dbHelper.getAllProducts();
        adapter.refreshData(updatedList);
    }
}
