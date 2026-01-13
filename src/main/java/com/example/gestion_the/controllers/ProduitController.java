package com.example.gestion_the.controllers;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.services.ProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public String listeProduits(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String typeFilter,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            Model model) {

        List<Produit> produits;

        if (search != null && !search.trim().isEmpty() && typeFilter != null && !typeFilter.trim().isEmpty()) {
            produits = produitService.findByNomAndTypeThe(search, typeFilter);
        } else if (search != null && !search.trim().isEmpty()) {
            produits = produitService.findByNom(search);
        } else if (typeFilter != null && !typeFilter.trim().isEmpty()) {
            produits = produitService.findByTypeThe(typeFilter);
        } else {
            produits = produitService.getAllProduits();
        }

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            boolean ascending = "asc".equalsIgnoreCase(direction);
            produits = sortProduits(produits, sortBy, ascending);
        }

        model.addAttribute("produits", produits);
        model.addAttribute("search", search);
        model.addAttribute("typeFilter", typeFilter);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "produits/liste";
    }

    @GetMapping("/nouveau")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("produit", new Produit());
        return "produits/formulaire";
    }

    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id, Model model) {
        Produit produit = produitService.getProduitById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));
        model.addAttribute("produit", produit);
        return "produits/formulaire";
    }

    private List<Produit> sortProduits(List<Produit> produits, String sortBy, boolean ascending) {
        return produits.stream()
                .sorted((p1, p2) -> {
                    int comparison = 0;
                    switch (sortBy.toLowerCase()) {
                        case "nom":
                            comparison = p1.getNom().compareToIgnoreCase(p2.getNom());
                            break;
                        case "prix":
                            comparison = p1.getPrix().compareTo(p2.getPrix());
                            break;
                        case "quantitestock":
                            comparison = p1.getQuantiteStock().compareTo(p2.getQuantiteStock());
                            break;
                        case "datereception":
                            comparison = p1.getDateReception().compareTo(p2.getDateReception());
                            break;
                        default:
                            comparison = 0;
                    }
                    return ascending ? comparison : -comparison;
                })
                .collect(Collectors.toList());
    }
}