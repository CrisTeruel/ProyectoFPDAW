package com.libreria.libreria_teruel_ayala.repository;

import com.libreria.libreria_teruel_ayala.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    // buscar autor por nombre exacto, lo necesitamos al añadir libros
    Autor findByNombre(String nombre);
}