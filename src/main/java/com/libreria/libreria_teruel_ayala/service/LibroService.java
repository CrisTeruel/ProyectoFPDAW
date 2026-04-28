package com.libreria.libreria_teruel_ayala.service;

import com.libreria.libreria_teruel_ayala.dto.GoogleBooksResponse;
import com.libreria.libreria_teruel_ayala.model.*;
import com.libreria.libreria_teruel_ayala.repository.*;
import com.libreria.libreria_teruel_ayala.util.IsbnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private EditorialRepository editorialRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private GoogleBooksService googleBooksService;


    public Libro añadirLibro(String isbn) {

        String isbn13 = IsbnUtils.toIsbn13(isbn);

        if (libroRepository.existsById(isbn13)) {
            throw new RuntimeException("El libro ya existe en la libreria");
        }

        GoogleBooksResponse.VolumeInfo info = googleBooksService.buscarPorIsbn(isbn13);
        if (info == null) {
            throw new RuntimeException("No se ha encontrado el libro en Google Books");
        }

        // editorial: Si no esta en bd la creo
        Editorial editorial = editorialRepository.findByNombre(info.publisher);
        if (editorial == null) {
            editorial = new Editorial();
            // A veces google no devuelve publisher, asi no peta el campo not null
            if (info.publisher != null) {
                editorial.setNombre(info.publisher);
            } else {
                editorial.setNombre("Desconocida");
            }
            editorial = editorialRepository.save(editorial);
        }

        Libro libro = new Libro();
        libro.setIsbn(isbn13);
        libro.setTitulo(info.title);
        libro.setDescripcion(info.description);
        libro.setEditorial(editorial);

        // La fecha a veces viene "2025-11-18" y otras solo "2025" y entonces parse explota
        if (info.publishedDate != null) {
            try {
                libro.setFechaPublicacion(LocalDate.parse(info.publishedDate));
            } catch (Exception e) {
                System.out.println("fecha rara: " + info.publishedDate);
            }
        }

        if (info.authors != null) {
            libro.setAutores(buscarOCrearAutores(info.authors));
        }

        // categorias - es practicamente igual que autores, en algun momento lo paso a metodo
        if (info.categories != null) {
            List<Categoria> cats = new ArrayList<>();
            for (String n : info.categories) {
                Categoria c = categoriaRepository.findByNombre(n);
                if (c == null) {
                    c = new Categoria();
                    c.setNombre(n);
                    c = categoriaRepository.save(c);
                }
                cats.add(c);
            }
            libro.setCategorias(cats);
        }

        if (info.imageLinks != null) {
            libro.setPortada(info.imageLinks.thumbnail);
        }

        return libroRepository.save(libro);
    }

    // saqué esto a metodo porque hacerlo dentro del añadirLibro me quedaba ilegible
    private List<Autor> buscarOCrearAutores(List<String> nombres) {
        List<Autor> autores = new ArrayList<>();
        for (String nombre : nombres) {
            Autor autor = autorRepository.findByNombre(nombre);
            if (autor == null) {
                autor = new Autor();
                autor.setNombre(nombre);
                autor = autorRepository.save(autor);
            }
            autores.add(autor);
        }
        return autores;
    }


    public void borrarLibro(String isbn) {
        String isbn13 = IsbnUtils.toIsbn13(isbn);
        if (!libroRepository.existsById(isbn13)) {
            throw new RuntimeException("El libro no existe en la libreria");
        }
        libroRepository.deleteById(isbn13);
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.findByAutoresNombreContainingIgnoreCase(autor);
    }

    public List<Libro> buscarPorCategoria(Integer categoriaId) {
        return libroRepository.findByCategorias_Id(categoriaId);
    }

    // devuelvo lista aunque sea 1 solo, asi la vista trata todas las busquedas igual
    public List<Libro> buscarPorIsbn(String isbn) {
        String isbn13 = IsbnUtils.toIsbn13(isbn);
        List<Libro> resultado = new ArrayList<>();
        Libro libro = libroRepository.findByIsbn(isbn13);
        if (libro != null) {
            resultado.add(libro);
        }
        return resultado;
    }

    public List<Libro> ultimos5() {
        return libroRepository.findTop5ByOrderByFechaPublicacionDesc();
    }

    public List<Libro> todos() {
        return libroRepository.findAll();
    }

    public List<Categoria> todasLasCategorias() {
        return categoriaRepository.findAll();
    }
}
