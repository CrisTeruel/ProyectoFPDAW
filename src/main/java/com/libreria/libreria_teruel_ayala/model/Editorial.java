package com.libreria.libreria_teruel_ayala.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "editorial")
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nombre;

    // una editorial tiene muchos libros
    @OneToMany(mappedBy = "editorial")
    private List<Libro> libros;
}