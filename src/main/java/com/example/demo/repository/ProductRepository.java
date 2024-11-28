package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Exemple de requête avec pagination
    Page<Product> findAll(Pageable pageable);

    // Requête personnalisée pour obtenir des produits par nom
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContains(String name);

    // Requête personnalisée avec jointure (si nécessaire pour d'autres entités liées)
    // Exemple : Récupérer des produits avec leur catégorie
    @Query("SELECT p FROM Product p WHERE p.price < :price")
    List<Product> findByPriceLessThan(@Param("price") double price);


    // Mise en cache des produits en fonction du nom (par exemple, produits populaires)
    @Cacheable(value = "productsCache", key = "#name")
    List<Product> findByName(String name);

    // Requête asynchrone pour récupérer tous les produits avec un certain prix
    @Async
    @Query("SELECT p FROM Product p WHERE p.price < :price")
    CompletableFuture<List<Product>> findByPriceLessThanAsync(double price);
}
