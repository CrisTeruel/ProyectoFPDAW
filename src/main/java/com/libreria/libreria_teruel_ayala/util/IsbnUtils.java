package com.libreria.libreria_teruel_ayala.util;

public class IsbnUtils {

        // La conversión de ISBN-10 a ISBN-13 tiene una fórmula matemática con dígitos de control
        // si ya es ISBN-13 lo devuelve tal cual, si es ISBN-10 lo transforma ISBN-13
        // para mandar a la API de google y guardar en bbdd
        // LA FORMULA DE ESTA CLASE ESTO ESTA COPIADO DE STACK OVERFLOW! no es cosa mía
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