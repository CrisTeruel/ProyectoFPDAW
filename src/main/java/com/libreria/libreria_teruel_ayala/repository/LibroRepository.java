package com.libreria.libreria_teruel_ayala.repository;

import com.libreria.libreria_teruel_ayala.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, String> {

    // buscar por titulo parcial
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // buscar por nombre de autor parcial
    List<Libro> findByAutoresNombreContainingIgnoreCase(String nombre);

    // buscar por categoria
    List<Libro> findByCategorias_Id(Integer categoriaId);

    // los ultimos 5 añadidos para la pagina de inicio
    // TODO: esto habria que ordenarlo por fecha de insercion, de momento queda asi
    List<Libro> findTop5ByOrderByFechaPublicacionDesc();
}