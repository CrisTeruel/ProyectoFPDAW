package com.libreria.libreria_teruel_ayala.service;

import com.libreria.libreria_teruel_ayala.dto.GoogleBooksResponse;
import com.libreria.libreria_teruel_ayala.util.IsbnUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public GoogleBooksResponse.VolumeInfo buscarPorIsbn(String isbn) {

        try {
            String isbn13 = IsbnUtils.toIsbn13(isbn);

            GoogleBooksResponse respuesta = restTemplate.getForObject(
                    URL + isbn13,
                    GoogleBooksResponse.class
            );

            if (respuesta == null || respuesta.items == null || respuesta.items.isEmpty()) {
                return null;
            }

            return respuesta.items.get(0).volumeInfo;

        } catch (Exception e) {
            // si peta google (timeout, 500, etc) devuelvo null y que el service lo controle
            System.out.println("error google books: " + e.getMessage());
            return null;
        }
    }
}
