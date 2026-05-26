package com.libreria.libreria_teruel_ayala.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IsbnUtilsTest {

    @Test
    void isbn10SeConvierteAIsbn13() {
        // el isbn10 de "El Hobbit"
        String isbn10 = "0547928228";
        String isbn13 = IsbnUtils.toIsbn13(isbn10);
        assertEquals("9780547928227", isbn13);
    }

    @Test
    void isbn13SeQuedaIgual() {
        String isbn13 = "9780547928227";
        String resultado = IsbnUtils.toIsbn13(isbn13);
        assertEquals("9780547928227", resultado);
    }

    @Test
    void isbnConGuionesSeLimpia() {
        // a veces vienen con guiones, tiene que funcionar igual
        String isbn = "978-0-547-92822-7";
        String resultado = IsbnUtils.toIsbn13(isbn);
        assertEquals("9780547928227", resultado);
    }

    @Test
    void isbnInvalidoLanzaExcepcion() {
        // un isbn que no tiene 10 ni 13 caracteres tiene que petar
        assertThrows(IllegalArgumentException.class, () -> {
            IsbnUtils.toIsbn13("12345");
        });
    }
}