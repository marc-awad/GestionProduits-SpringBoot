package com.example.gestion_the.controllers;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.services.ProduitService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public String listeProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String typeFilter,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {

        Page<Produit> pageProduits = produitService.getProduitsPaged(
                search, typeFilter, page, size, sortBy, direction
        );

        model.addAttribute("produits", pageProduits.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageProduits.getTotalPages());

        model.addAttribute("search", search);
        model.addAttribute("typeFilter", typeFilter);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

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
    public String sauvegarderProduit(
            @Valid @ModelAttribute Produit produit,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            return "formulaire";
        }

        produitService.saveProduit(produit);
        redirectAttributes.addFlashAttribute("successMessage", "Produit enregistré avec succès !");
        return "redirect:/produits";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimerProduit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        produitService.deleteProduit(id);
        redirectAttributes.addFlashAttribute("successMessage", "Produit supprimé avec succès !");
        return "redirect:/produits";
    }

    @GetMapping("/export")
    public void exportCsv(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String typeFilter,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=produits.csv"
        );

        List<Produit> produits = produitService
                .getProduitsPaged(search, typeFilter, 0, Integer.MAX_VALUE, sortBy, direction)
                .getContent();

        PrintWriter writer = response.getWriter();
        writer.println("ID,Nom,Type,Origine,Prix,Stock,Date réception");

        for (Produit p : produits) {
            writer.printf(
                    "%d,%s,%s,%s,%s,%d,%s%n",
                    p.getId(),
                    p.getNom(),
                    p.getTypeThe(),
                    p.getOrigine(),
                    p.getPrix(),
                    p.getQuantiteStock(),
                    p.getDateReception()
            );
        }

        writer.flush();
    }

}
