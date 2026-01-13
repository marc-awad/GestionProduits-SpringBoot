package com.example.gestion_the.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "Le type de thé est obligatoire")
    @Size(max = 50, message = "Le type de thé ne peut pas dépasser 50 caractères")
    private String typeThe;

    @NotBlank(message = "L'origine est obligatoire")
    @Size(max = 50, message = "L'origine ne peut pas dépasser 50 caractères")
    private String origine;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "5.0", message = "Le prix doit être au moins 5")
    @DecimalMax(value = "100.0", message = "Le prix ne peut pas dépasser 100")
    private BigDecimal prix;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité en stock ne peut pas être négative")
    private Integer quantiteStock;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @NotNull(message = "La date de réception est obligatoire")
    private LocalDate dateReception;
}
