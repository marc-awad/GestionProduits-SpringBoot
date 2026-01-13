package com.example.gestion_the.services;

import com.example.gestion_the.models.Produit;
import com.example.gestion_the.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


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
    public Page<Produit> getProduitsPaged(
            String search,
            String typeFilter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = Sort.unsorted();

        if (sortBy != null && !sortBy.isBlank()) {
            sort = "desc".equalsIgnoreCase(direction)
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        if (search != null && !search.isBlank()
                && typeFilter != null && !typeFilter.isBlank()) {
            return produitRepository
                    .findByNomIgnoreCaseContainingAndTypeThe(search, typeFilter, pageable);
        }

        if (search != null && !search.isBlank()) {
            return produitRepository
                    .findByNomIgnoreCaseContaining(search, pageable);
        }

        if (typeFilter != null && !typeFilter.isBlank()) {
            return produitRepository
                    .findByTypeThe(typeFilter, pageable);
        }

        return produitRepository.findAll(pageable);
    }

}
