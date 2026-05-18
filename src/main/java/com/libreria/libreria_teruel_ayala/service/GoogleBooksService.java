package com.libreria.libreria_teruel_ayala.service;

import com.libreria.libreria_teruel_ayala.dto.GoogleBooksResponse;
import com.libreria.libreria_teruel_ayala.util.IsbnUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_KEY = "AIzaSyASKdu2WPJ9LHKTr4sevRMQH-_LTuXih9g";
    private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public GoogleBooksResponse.VolumeInfo buscarPorIsbn(String isbn) {
        try {
            String isbn13 = IsbnUtils.toIsbn13(isbn);

            String urlCompleta = URL + isbn13 + "&key=" + API_KEY;

            GoogleBooksResponse respuesta = restTemplate.getForObject(
                    urlCompleta,
                    GoogleBooksResponse.class
            );

            if (respuesta == null || respuesta.items == null || respuesta.items.isEmpty()) {
                return null;
            }

            return respuesta.items.get(0).volumeInfo;

        } catch (Exception e) {
            // si la api peta devolvemos null
            System.out.println("Error llamando a Google Books: " + e.getMessage());
            return null;
        }
    }
}