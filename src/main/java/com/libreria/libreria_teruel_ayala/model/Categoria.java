package com.libreria.libreria_teruel_ayala.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 80)
    private String nombre;

    // muchos a muchos con libro
    @ManyToMany(mappedBy = "categorias")
    private List<Libro> libros;
}
