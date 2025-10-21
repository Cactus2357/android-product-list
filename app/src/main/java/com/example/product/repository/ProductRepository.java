package com.example.product.repository;

import android.content.Context;

import com.example.product.AppDatabase;
import com.example.product.dao.ProductDao;
import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(Context context) {
        this.productDao = AppDatabase.getInstance(context).productDao();
    }

    public List<ProductDto> getAllProducts() {
        return productDao.getAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(int productId) {
        Product product = productDao.getById(productId);
        return mapToDto(product);
    }

    public void insertProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productDao.insert(product);
    }

    public void updateProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productDao.update(product);
    }

    public void deleteProduct(int productId) {
        Product product = new Product();
        product.setId(productId);
        productDao.delete(product);
    }

    private ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
}
