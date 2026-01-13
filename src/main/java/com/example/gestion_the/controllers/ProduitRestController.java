package com.example.gestion_the.controllers;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.services.ProduitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitRestController {

    private final ProduitService produitService;

    public ProduitRestController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<Produit> getAll(@RequestParam(required = false) String sortField,
                                @RequestParam(required = false, defaultValue = "true") boolean asc) {
        if (sortField != null) {
            return produitService.getAllProduitsSorted(sortField, asc);
        }
        return produitService.getAllProduits();
    }

    @GetMapping("/{id}")
    public Produit getById(@PathVariable Long id) {
        return produitService.getProduitById(id).orElse(null);
    }

    @PostMapping
    public Produit create(@RequestBody Produit produit) {
        return produitService.saveProduit(produit);
    }

    @PutMapping("/{id}")
    public Produit update(@PathVariable Long id, @RequestBody Produit produit) {
        produit.setId(id);
        return produitService.saveProduit(produit);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        produitService.deleteProduit(id);
    }

    @GetMapping("/search/nom")
    public List<Produit> searchByNom(@RequestParam String nom) {
        return produitService.findByNom(nom);
    }

    @GetMapping("/search/type")
    public List<Produit> filterByType(@RequestParam String typeThe) {
        return produitService.findByTypeThe(typeThe);
    }

    @GetMapping("/search/nom-type")
    public List<Produit> searchByNomAndType(@RequestParam String nom, @RequestParam String typeThe) {
        return produitService.findByNomAndTypeThe(nom, typeThe);
    }
}
