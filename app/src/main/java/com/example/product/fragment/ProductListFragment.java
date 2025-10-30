package com.example.product.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.product.adapter.ProductAdapter;
import com.example.product.databinding.FragmentProductListBinding;
import com.example.product.dto.ProductDto;
import com.example.product.repository.ProductRepository;

import java.util.List;

public class ProductListFragment extends Fragment {
    private FragmentProductListBinding binding;
    private ProductRepository productRepository;
    private ProductAdapter adapter;
    private boolean enableItemMenu = false;

    public ProductListFragment setEnableItemMenu(boolean enableItemMenu) {
        this.enableItemMenu = enableItemMenu;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productRepository = new ProductRepository(requireContext());

        List<ProductDto> products = productRepository.getAllProducts();
        adapter = new ProductAdapter(products, productRepository, this::refreshList);
        adapter.setEnableItemMenu(enableItemMenu);
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewProducts.setAdapter(adapter);
    }

    private void refreshList() {
        adapter.refreshData(productRepository.getAllProducts());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
