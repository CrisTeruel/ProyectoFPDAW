-- ============================================================
-- Base de datos: Librería Teruel Ayala
-- Autor: Cristina Teruel Ayala
-- Fecha: 2026
-- Motor: MySQL 8 o superior
-- ============================================================

DROP DATABASE IF EXISTS libreria_teruel_ayala;

CREATE DATABASE IF NOT EXISTS libreria_teruel_ayala
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE libreria_teruel_ayala;

-- ------------------------------------------------------------
-- Tabla: editorial
-- Una editorial publica muchos libros (1:N con libro)
-- ------------------------------------------------------------
CREATE TABLE editorial (
    id      INT             NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(120)    NOT NULL,
    PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Tabla: autor
-- Un autor puede escribir muchos libros (N:M con libro)
-- ------------------------------------------------------------
CREATE TABLE autor (
    id      INT             NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(120)    NOT NULL,
    PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Tabla: categoria
-- Un libro puede pertenecer a varias categorias (N:M con libro)
-- ------------------------------------------------------------
CREATE TABLE categoria (
    id      INT             NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(80)     NOT NULL,
    PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Tabla: libro
-- Clave primaria es el ISBN-13 (unico por edicion, compatible con la API externa)
-- ------------------------------------------------------------
CREATE TABLE libro (
    isbn                VARCHAR(13)     NOT NULL,
    titulo              VARCHAR(255)    NOT NULL,
    descripcion         TEXT,
    fecha_publicacion   DATE,
    portada             VARCHAR(500),
    editorial_id        INT             NOT NULL,
    PRIMARY KEY (isbn),
    CONSTRAINT fk_libro_editorial
        FOREIGN KEY (editorial_id) REFERENCES editorial (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT  -- no borrar editorial si tiene libros
);

-- ------------------------------------------------------------
-- Tabla intermedia: libro_autor (N:M entre libro y autor)
-- Un libro puede tener varios autores y viceversa
-- ------------------------------------------------------------
CREATE TABLE libro_autor (
    libro_isbn  VARCHAR(13)     NOT NULL,
    autor_id    INT             NOT NULL,
    PRIMARY KEY (libro_isbn, autor_id),  -- clave compuesta
    CONSTRAINT fk_libroautor_libro
        FOREIGN KEY (libro_isbn) REFERENCES libro (isbn)
        ON UPDATE CASCADE
        ON DELETE CASCADE,  -- si borramos libro, limpiamos esto
    CONSTRAINT fk_libroautor_autor
        FOREIGN KEY (autor_id) REFERENCES autor (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Tabla intermedia: libro_categoria (N:M entre libro y categoria)
-- ------------------------------------------------------------
CREATE TABLE libro_categoria (
    libro_isbn      VARCHAR(13)     NOT NULL,
    categoria_id    INT             NOT NULL,
    PRIMARY KEY (libro_isbn, categoria_id),  -- clave compuesta
    CONSTRAINT fk_librocategoria_libro
        FOREIGN KEY (libro_isbn) REFERENCES libro (isbn)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_librocategoria_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
