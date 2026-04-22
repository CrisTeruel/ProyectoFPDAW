package com.libreria.libreria_teruel_ayala.repository;

import com.libreria.libreria_teruel_ayala.model.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorialRepository extends JpaRepository<Editorial, Integer> {

    // buscar editorial por nombre para no duplicar
    Editorial findByNombre(String nombre);
}