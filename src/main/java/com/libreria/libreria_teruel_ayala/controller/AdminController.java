package com.libreria.libreria_teruel_ayala.controller;

import com.libreria.libreria_teruel_ayala.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public String admin(Model model) {
        // mostramos todos los libros en la vista de admin
        model.addAttribute("libros", libroService.todos());
        return "admin";
    }

    @PostMapping("/añadir")
    public String añadirLibro(@RequestParam String isbn, Model model) {
        try {
            libroService.añadirLibro(isbn);
            model.addAttribute("mensaje", "Libro añadido correctamente");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("libros", libroService.todos());
        return "admin";
    }

    @PostMapping("/borrar")
    public String borrarLibro(@RequestParam String isbn, Model model) {
        try {
            libroService.borrarLibro(isbn);
            model.addAttribute("mensaje", "Libro eliminado correctamente");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("libros", libroService.todos());
        return "admin";
    }
}