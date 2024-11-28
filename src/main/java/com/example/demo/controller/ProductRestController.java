package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@EnableCaching  // Assurez-vous que le caching est activé dans la configuration
public class ProductRestController {

    @Autowired
    private ProductService productService;

    // Crée un produit - Asynchrone et validation des entrées
    @PostMapping
    @Async  // Traitement en arrière-plan pour ne pas bloquer l'appel principal
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Récupère tous les produits sans pagination

   @Cacheable(value = "productsCache", key = "'allProducts'")
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Récupère un produit par ID - avec cache
    @GetMapping("/{id}")
   @Cacheable(value = "productCache", key = "#id")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Met à jour un produit - Asynchrone et validation des entrées
    @PutMapping("/{id}")
    @Async
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    // Supprime un produit - Asynchrone
    @DeleteMapping("/{id}")
    @Async
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
