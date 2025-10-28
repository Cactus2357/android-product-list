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
        if (productDao.getAll().isEmpty()) {
            addDummyProducts();
        }
    }

    public List<ProductDto> getAllProducts() {
        return productDao.getAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private void addDummyProducts() {
        insertProduct(new ProductDto("Laptop", 999.99));
        insertProduct(new ProductDto("Keyboard", 49.99));
        insertProduct(new ProductDto("Mouse", 29.99));
        insertProduct(new ProductDto("Monitor", 199.99));
        insertProduct(new ProductDto("Headphones", 89.99));
        insertProduct(new ProductDto("Smartphone", 699.99));
        insertProduct(new ProductDto("USB Cable", 9.99));
    }

    public ProductDto getProductById(int productId) {
        Product product = productDao.getById(productId);
        return mapToDto(product);
    }

    public void insertProduct(ProductDto productDto) {
        Product product = new Product(productDto.getName(), productDto.getPrice());
        productDao.insert(product);
    }

    public void updateProduct(ProductDto productDto) {
        Product product = new Product(productDto.getId(), productDto.getName(), productDto.getPrice());
        productDao.update(product);
    }

    public void deleteProduct(int productId) {
        Product product = productDao.getById(productId);
        if (product != null) {
            productDao.delete(product);
        }
    }

    private ProductDto mapToDto(Product product) {
        if (product == null) return null;
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
}
