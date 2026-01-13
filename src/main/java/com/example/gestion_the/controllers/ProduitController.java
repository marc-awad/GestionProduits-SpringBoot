package com.example.gestion_the.controllers;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.services.ProduitService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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

        List<Produit> produits = new ArrayList<>();

        try {
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
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des produits : " + e.getMessage());
            model.addAttribute("errorMessage", "Erreur lors du chargement des produits");
        }

        model.addAttribute("produits", produits);
        model.addAttribute("search", search);
        model.addAttribute("typeFilter", typeFilter);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        System.out.println("DEBUG: Nombre de produits chargés = " + produits.size());

        return "index";
    }

    @GetMapping("/nouveau")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("produit", new Produit());
        return "formulaire";
    }

    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id, Model model) {
        Produit produit = produitService.getProduitById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + id));
        model.addAttribute("produit", produit);
        return "formulaire";
    }

    @PostMapping("/sauvegarder")
    public String sauvegarderProduit(@Valid @ModelAttribute Produit produit,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            return "formulaire";
        }

        try {
            produitService.saveProduit(produit);

            if (produit.getId() == null) {
                redirectAttributes.addFlashAttribute("successMessage", "Produit ajouté avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Produit modifié avec succès !");
            }

            return "redirect:/produits";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la sauvegarde : " + e.getMessage());
            return "redirect:/produits";
        }
    }

    @GetMapping("/supprimer/{id}")
    public String supprimerProduit(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            if (!produitService.getProduitById(id).isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Produit introuvable avec l'id : " + id);
                return "redirect:/produits";
            }

            produitService.deleteProduit(id);
            redirectAttributes.addFlashAttribute("successMessage", "Produit supprimé avec succès !");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }

        return "redirect:/produits";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimerProduitPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return supprimerProduit(id, redirectAttributes);
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