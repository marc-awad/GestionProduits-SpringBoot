package com.example.gestion_the.repositories;

import com.example.gestion_the.models.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Existant (non paginé)
    List<Produit> findByNomIgnoreCase(String nom);

    List<Produit> findByTypeThe(String typeThe);

    List<Produit> findByNomIgnoreCaseAndTypeThe(String nom, String typeThe);

    // Pagination
    Page<Produit> findByNomIgnoreCaseContaining(String nom, Pageable pageable);

    Page<Produit> findByTypeThe(String typeThe, Pageable pageable);

    Page<Produit> findByNomIgnoreCaseContainingAndTypeThe(
            String nom,
            String typeThe,
            Pageable pageable
    );
}
