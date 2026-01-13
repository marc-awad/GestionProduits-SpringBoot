package com.example.gestion_the.config;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.repositories.ProduitRepository;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {

    private final ProduitRepository produitRepository;

    public DataInitializer(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @PostConstruct
    public void initData() {

        // 🔥 Vider la table avant de réinsérer
        produitRepository.deleteAll();

        List<Produit> produits = new ArrayList<>();

        String[] types = {"Vert", "Noir", "Blanc", "Oolong", "Rouge"};
        String[] origines = {"Japon", "Chine", "Inde", "Sri Lanka", "Afrique du Sud"};
        String[] noms = {
                "Sencha", "Matcha", "Assam", "Darjeeling", "Pai Mu Tan",
                "Silver Needle", "Tie Guan Yin", "Wuyi", "Rooibos", "Honeybush",
                "Earl Grey", "Jasmine Pearl", "Long Jing", "Keemun", "Gunpowder",
                "Ceylon", "Lapsang Souchong", "Chamomile", "Peppermint", "Hibiscus",
                "Oolong Milk", "Ginseng Oolong", "Black Dragon", "White Peony", "Red Bush",
                "Green Pearl", "Dragonwell", "Formosa Oolong", "Royal Assam", "Golden Monkey"
        };

        for (int i = 0; i < 30; i++) {
            String nomProduit = "Thé " + noms[i];
            String type = types[i % types.length];
            String origine = origines[i % origines.length];
            BigDecimal prix = BigDecimal.valueOf(5 + (i % 10) * 0.5); // 5.0 à 9.5 €
            int stock = 10 + (i * 3 % 50); // stock variable entre 10 et 59
            String description = "Description du " + nomProduit + ", un thé de type " + type;
            LocalDate dateReception = LocalDate.now().minusDays(i);

            produits.add(new Produit(
                    null, nomProduit, type, origine, prix, stock, description, dateReception
            ));
        }

        produitRepository.saveAll(produits);
        System.out.println("💚 Base réinitialisée avec 30 produits pour tests et pagination !");
    }
}
