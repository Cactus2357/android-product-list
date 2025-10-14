package com.example.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product.databinding.ItemProductBinding;
import com.example.product.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.binding.tvId.setText(String.valueOf(p.getId()));
        holder.binding.tvName.setText(p.getName());
        holder.binding.tvPrice.setText(String.format("$%.2f", p.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void refreshData(List<Product> list) {
        this.productList = list;
        notifyDataSetChanged();
    }

}
