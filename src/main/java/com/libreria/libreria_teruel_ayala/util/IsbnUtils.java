package com.libreria.libreria_teruel_ayala.util;

public class IsbnUtils {

    // pasa de ISBN-10 a ISBN-13. Si ya es 13 lo devuelve tal cual.
    // la formula del digito de control la copié de stackoverflow, no me preguntes
    public static String toIsbn13(String isbn) {
        isbn = isbn.replaceAll("-", "").trim();

        if (isbn.length() == 13) {
            return isbn;
        }

        if (isbn.length() == 10) {
            String base = "978" + isbn.substring(0, 9);

            int suma = 0;
            for (int i = 0; i < 12; i++) {
                int factor = (i % 2 == 0) ? 1 : 3;
                suma += factor * Character.getNumericValue(base.charAt(i));
            }
            int control = (10 - (suma % 10)) % 10;

            return base + control;
        }

        throw new IllegalArgumentException("ISBN no valido: " + isbn);
    }
}
