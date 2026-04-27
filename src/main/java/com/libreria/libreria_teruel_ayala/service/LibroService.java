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

    // añadir libro a partir de un ISBN
    public Libro añadirLibro(String isbn) {

        String isbn13 = IsbnUtils.toIsbn13(isbn);

        // comprobamos que no este ya en la bbdd
        if (libroRepository.existsById(isbn13)) {
            throw new RuntimeException("El libro ya existe en la libreria");
        }

        // consultamos la api
        GoogleBooksResponse.VolumeInfo info = googleBooksService.buscarPorIsbn(isbn13);

        // si peta, la api devuelve null en info
        if (info == null) {
            throw new RuntimeException("No se ha encontrado el libro en Google Books");
        }

        // buscamos o creamos la editorial
        Editorial editorial = editorialRepository.findByNombre(info.publisher);
        if (editorial == null) {
            editorial = new Editorial();
            if (info.publisher != null) { //Si google respondio que encontro el libro, pero la editorial sale nula, le ponemos Desconocida
                editorial.setNombre(info.publisher);
            } else {
                editorial.setNombre("Desconocida");
            }
            editorial = editorialRepository.save(editorial);
        }

        // creamos el libro
        Libro libro = new Libro();
        libro.setIsbn(isbn13);
        libro.setTitulo(info.title);
        libro.setDescripcion(info.description);
        libro.setEditorial(editorial);

        // la fecha viene como string "2025-11-18" o a veces solo "2025"
        if (info.publishedDate != null) {
            try {
                libro.setFechaPublicacion(LocalDate.parse(info.publishedDate));
            } catch (Exception e) {
                // si solo viene el año no podemos parsear, dejamos null
                System.out.println("Fecha no parseable: " + info.publishedDate);
            }
        }

        // autores
        if (info.authors != null) {
            List<Autor> autores = new ArrayList<>();
            // buscamos si ya existe el autor antes de darlo de alta
            for (String nombreAutor : info.authors) {
                Autor autor = autorRepository.findByNombre(nombreAutor);
                if (autor == null) {
                    autor = new Autor();
                    autor.setNombre(nombreAutor);
                    autor = autorRepository.save(autor);
                }
                autores.add(autor);
            }
            libro.setAutores(autores);
        }

        // categorias
        if (info.categories != null) {
            List<Categoria> categorias = new ArrayList<>();
            // buscamos si ya existe la categoria antes de darla de alta
            for (String nombreCategoria : info.categories) {
                Categoria categoria = categoriaRepository.findByNombre(nombreCategoria);
                if (categoria == null) {
                    categoria = new Categoria();
                    categoria.setNombre(nombreCategoria);
                    categoria = categoriaRepository.save(categoria);
                }
                categorias.add(categoria);
            }
            libro.setCategorias(categorias);
        }

        // guardamos la url del thumbnail
        if (info.imageLinks != null) {
            libro.setPortada(info.imageLinks.thumbnail);
        }

        return libroRepository.save(libro);
    }

    // borrar libro por ISBN
    public void borrarLibro(String isbn) {
        String isbn13 = IsbnUtils.toIsbn13(isbn);
        if (!libroRepository.existsById(isbn13)) {
            throw new RuntimeException("El libro no existe en la libreria");
        }
        libroRepository.deleteById(isbn13);
    }

    // busquedas
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.findByAutoresNombreContainingIgnoreCase(autor);
    }

    public List<Libro> buscarPorCategoria(Integer categoriaId) {
        return libroRepository.findByCategorias_Id(categoriaId);
    }

    public List<Libro> buscarPorIsbn(String isbn) {
        String isbn13 = IsbnUtils.toIsbn13(isbn);
        List<Libro> resultado = new ArrayList<>();

        Libro libro = libroRepository.findByIsbn(isbn13);
        if (libro != null) {
            resultado.add(libro);
        }

        return resultado;
    }

    // ultimos 5 para la pagina de inicio
    public List<Libro> ultimos5() {
        return libroRepository.findTop5ByOrderByFechaPublicacionDesc();
    }

    // todos los libros
    public List<Libro> todos() {
        return libroRepository.findAll();
    }

    // todas las categorias para el desplegable
    public List<Categoria> todasLasCategorias() {
        return categoriaRepository.findAll();
    }
}