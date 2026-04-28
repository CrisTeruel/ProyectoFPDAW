package com.libreria.libreria_teruel_ayala.controller;

import com.libreria.libreria_teruel_ayala.service.LibroService;
import com.libreria.libreria_teruel_ayala.repository.LibroRepository;
import com.libreria.libreria_teruel_ayala.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BusquedaController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/busqueda")
    public String busqueda(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Integer categoriaId,
            Model model) {

        // Categorias para el desplegable
        model.addAttribute("categorias", libroService.todasLasCategorias());

        // si entran sin buscar nada
        if (titulo == null && autor == null && isbn == null && categoriaId == null) {
            return "busqueda";
        }

        // orden de prioridad: isbn, titulo, autor, categoria
        if (isbn != null && !isbn.isEmpty()) {
            model.addAttribute("resultados", libroService.buscarPorIsbn(isbn));
        } else if (titulo != null && !titulo.isEmpty()) {
            model.addAttribute("resultados", libroService.buscarPorTitulo(titulo));
        } else if (autor != null && !autor.isEmpty()) {
            model.addAttribute("resultados", libroService.buscarPorAutor(autor));
        } else if (categoriaId != null) {
            model.addAttribute("resultados", libroService.buscarPorCategoria(categoriaId));
        }

        return "busqueda";
    }

    @GetMapping("/libro/{isbn}")
    public String detalle(@PathVariable String isbn, Model model) {
        Libro libro = libroRepository.findByIsbn(isbn);
        if (libro == null) {
            return "redirect:/busqueda";
        }
        model.addAttribute("libro", libro);
        return "libro";
    }
}
