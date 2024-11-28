package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(length = 500)
    private String description;

    // Optimisation de la gestion des relations (exemple)
    // Si Product a une relation avec une autre entité (comme Category), utiliser LAZY si possible
    // @ManyToOne(fetch = FetchType.LAZY)
    // private Category category;

    // Si vous avez besoin de caches au niveau des entités, vous pouvez ajouter la gestion du cache
    // @Cacheable(value = "productCache")
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
}
