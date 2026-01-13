package com.example.gestion_the.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Redirection de la page d'accueil vers /produits
    @GetMapping("/")
    public String home() {
        return "redirect:/produits";
    }
}