package com.example.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product.databinding.ItemProductBinding;
import com.example.product.dto.ProductDto;
import com.example.product.repository.ProductRepository;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductDto> productList;
    private final ProductRepository productRepository;
    private final Runnable refreshCallback;

    public ProductAdapter(List<ProductDto> productList, ProductRepository productRepository, Runnable refreshCallback) {
        this.productList = productList;
        this.productRepository = productRepository;
        this.refreshCallback = refreshCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
    public int getItemCount() {
        return productList.size();
    }

    public void refreshData(List<ProductDto> list) {
        this.productList = list;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductDto p = productList.get(position);
//        holder.binding.tvId.setText(String.valueOf(p.getId()));
        holder.binding.tvName.setText(p.getName());
        holder.binding.tvPrice.setText(String.format("$%.2f", p.getPrice()));

        holder.binding.getRoot().setOnClickListener(v -> showItemMenu(v, p));
    }

    private void showItemMenu(View v, ProductDto p) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_product_item, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                showEditDialog(v, p);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                productRepository.deleteProduct(p.getId());
                refreshCallback.run();
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void showEditDialog(View v, ProductDto p) {
        View dialogView = LayoutInflater.from(v.getContext())
                .inflate(R.layout.dialog_form_product, null);
        EditText etName = dialogView.findViewById(R.id.etAddName);
        EditText etPrice = dialogView.findViewById(R.id.etAddPrice);

        etName.setText(p.getName());
        etPrice.setText(String.valueOf(p.getPrice()));

        new AlertDialog.Builder(v.getContext())
                .setTitle("Edit Product")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    p.setName(etName.getText().toString().trim());
                    p.setPrice(Double.parseDouble(etPrice.getText().toString().trim()));
                    productRepository.updateProduct(p);
                    refreshCallback.run();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}
