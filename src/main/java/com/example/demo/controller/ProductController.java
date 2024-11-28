package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Liste des produits sans pagination
    @GetMapping("/products")
    @Cacheable(value = "productsCache", key = "#root.method.name")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();  // Récupérer tous les produits sans pagination
        model.addAttribute("products", products);
        return "products/list";
    }

    // Page d'accueil avec le total des produits et la dernière mise à jour
    @GetMapping("/")
    @Cacheable(value = "homePageCache", key = "'homePage'")
    public String homePage(Model model) {
        model.addAttribute("totalProducts", productService.getAllProducts().size());  // Nombre total de produits
        model.addAttribute("lastUpdate", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );
        return "home";
    }

    // Formulaire de création de produit
    @GetMapping("/products/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/create";
    }

    // Création d'un produit
    @PostMapping("/products/new")
    public String createProduct(@ModelAttribute Product product) {
        productService.createProduct(product);
        sendEmailConfirmation("example@example.com");  // Envoi asynchrone d'un email de confirmation
        return "redirect:/";
    }

    // Formulaire d'édition de produit
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id"));
        model.addAttribute("product", product);
        return "products/edit";
    }

    // Mise à jour d'un produit
    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/products";
    }

    // Suppression d'un produit
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // Méthode asynchrone pour envoyer un email de confirmation
    @Async
    public void sendEmailConfirmation(String email) {
        // Code pour envoyer un email de confirmation
    }
}
