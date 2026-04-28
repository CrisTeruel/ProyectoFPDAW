package com.libreria.libreria_teruel_ayala.controller;

import com.libreria.libreria_teruel_ayala.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @Autowired
    private LibroService libroService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("libros", libroService.ultimos5());
        return "inicio";
    }
}
