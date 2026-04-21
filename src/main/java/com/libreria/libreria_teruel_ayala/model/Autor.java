package com.libreria.libreria_teruel_ayala.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nombre;

    // un autor puede tener muchos libros y viceversa
    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros;
}