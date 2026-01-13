package com.example.gestion_the.services;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public Produit saveProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> findByNom(String nom) {
        return produitRepository.findByNomIgnoreCase(nom);
    }

    public List<Produit> findByTypeThe(String typeThe) {
        return produitRepository.findByTypeThe(typeThe);
    }

    public List<Produit> findByNomAndTypeThe(String nom, String typeThe) {
        return produitRepository.findByNomIgnoreCaseAndTypeThe(nom, typeThe);
    }

    public List<Produit> getAllProduitsSorted(String fieldName, boolean ascending) {
        Sort sort = ascending ? Sort.by(fieldName).ascending() : Sort.by(fieldName).descending();
        return produitRepository.findAll(sort);
    }
}
