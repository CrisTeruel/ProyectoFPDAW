package com.libreria.libreria_teruel_ayala.repository;

import com.libreria.libreria_teruel_ayala.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, String> {

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByAutoresNombreContainingIgnoreCase(String nombre);

    List<Libro> findByCategorias_Id(Integer categoriaId);

    Libro findByIsbn(String isbn);

    // TODO ordenar bien
    List<Libro> findTop5ByOrderByFechaPublicacionDesc();
}
