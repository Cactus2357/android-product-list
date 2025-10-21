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
import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    ActivityListProductBinding binding;
    AppDatabase db;
    ProductRepository productRepository;
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

        productRepository = new ProductRepository(this);

        if (productRepository.getAllProducts().isEmpty()) {
            productRepository.insertProduct(new ProductDto("Laptop", 999.99));
            productRepository.insertProduct(new ProductDto("Keyboard", 49.99));
            productRepository.insertProduct(new ProductDto("Mouse", 29.99));
            productRepository.insertProduct(new ProductDto("Monitor", 199.99));
            productRepository.insertProduct(new ProductDto("Headphones", 89.99));
            productRepository.insertProduct(new ProductDto("Smartphone", 699.99));
            productRepository.insertProduct(new ProductDto("USB Cable", 9.99));
        }

        List<ProductDto> products = productRepository.getAllProducts();
        adapter = new ProductAdapter(products, productRepository, this::refreshList);
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewProducts.setAdapter(adapter);
    }

    private void refreshList() {
        adapter.refreshData(productRepository.getAllProducts());
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
                        productRepository.insertProduct(new ProductDto(name, price));
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
