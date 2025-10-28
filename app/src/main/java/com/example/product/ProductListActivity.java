package com.example.product;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.product.adapter.ProductAdapter;
import com.example.product.databinding.ActivityProductListBinding;
import com.example.product.fragment.ProductListFragment;

public class ProductListActivity extends AppCompatActivity {
    private ActivityProductListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Product List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new ProductListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
