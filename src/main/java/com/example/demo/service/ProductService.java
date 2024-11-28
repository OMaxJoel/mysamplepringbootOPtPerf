package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Read All without pagination and with caching
    @Cacheable(value = "productsCache", key = "#root.method.name")
    public List<Product> getAllProducts() {
        return productRepository.findAll();  // Récupère tous les produits sans pagination
    }

    // Read One with caching
    @Cacheable(value = "productCache", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Update
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());

        return productRepository.save(product);
    }

    // Delete
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    // Example Async method
    @Async
    public void sendEmailConfirmation(String email) {
        // Code for sending email confirmation
    }
}
