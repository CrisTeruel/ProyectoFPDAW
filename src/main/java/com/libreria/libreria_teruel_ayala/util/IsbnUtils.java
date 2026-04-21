package com.libreria.libreria_teruel_ayala.util;

public class IsbnUtils {

    // convierte ISBN-13 a ISBN-10
    // La conversión de ISBN-13 a ISBN-10 tiene una fórmula matemática con dígitos de control, permitiremos al admin usar ambas, en BBDD lo guardaremos en ISBN10 pero a google lo atacamos con ISBN13.
    // si ya es ISBN-10 lo devuelve tal cual
    public static String toIsbn10(String isbn) {
        isbn = isbn.replaceAll("-", "").trim();

        if (isbn.length() == 10) {
            return isbn;
        }

        if (isbn.length() == 13) {
            // quitamos los 3 primeros digitos (978 o 979) y el ultimo
            String core = isbn.substring(3, 12);

            // calculamos el digito de control del ISBN-10
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                suma += (10 - i) * Character.getNumericValue(core.charAt(i));
            }
            int control = (11 - (suma % 11)) % 11;

            // si el control es 10 se representa como X
            String digitoControl = control == 10 ? "X" : String.valueOf(control);

            return core + digitoControl;
        }

        // si no es ni 10 ni 13 algo va mal
        throw new IllegalArgumentException("ISBN no valido: " + isbn);
    }

    // devuelve siempre ISBN-13 para mandar a la API de google
    // google devuelve mejores resultados con ISBN-13
    public static String toIsbn13(String isbn) {
        isbn = isbn.replaceAll("-", "").trim();

        if (isbn.length() == 13) {
            return isbn;
        }

        if (isbn.length() == 10) {
            // añadimos prefijo 978 y recalculamos digito de control
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