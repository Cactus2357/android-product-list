package com.example.product.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.product.databinding.ActivityProductListBinding;
import com.example.product.entity.Product;
import com.example.product.fragment.ProductListFragment;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private ActivityProductListBinding binding;

    private List<Product> productList = new ArrayList<>();

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
                    .replace(binding.fragmentContainer.getId(), new ProductListFragment().setEnableItemMenu(true))
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    private void GetPost() {
//        String baseUrl = "https://jsonplaceholder.typicode.com";
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        IPostAPI productAPI = retrofit.create(IPostAPI.class);
////        productAPI.getProducts().enqueue(new Callback<List<Product>>() {
////            @Override
////            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
////                if (response.isSuccessful()) {
////                    productList.clear();
////                    productList.addAll(response.body());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<Product>> call, Throwable t) {
////                t.printStackTrace();
////            }
////        });
//
//    }

}
