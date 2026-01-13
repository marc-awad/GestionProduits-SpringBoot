package com.example.gestion_the.repositories;

import com.example.gestion_the.models.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByNomIgnoreCase(String nom);

    List<Produit> findByTypeThe(String typeThe);

    List<Produit> findByNomIgnoreCaseAndTypeThe(String nom, String typeThe);

}
