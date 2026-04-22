package com.libreria.libreria_teruel_ayala.service;

import com.libreria.libreria_teruel_ayala.dto.GoogleBooksResponse;
import com.libreria.libreria_teruel_ayala.util.IsbnUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    // RestTemplate es el cliente HTTP de Spring para llamar a APIs externas
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public GoogleBooksResponse.VolumeInfo buscarPorIsbn(String isbn) {

        try {
            // convertimos a ISBN-13 SIEMPRE antes de consultar
            String isbn13 = IsbnUtils.toIsbn13(isbn);

            GoogleBooksResponse respuesta = restTemplate.getForObject(
                    URL + isbn13,
                    GoogleBooksResponse.class
            );

            // si no encuentra nada devuelve null
            if (respuesta == null || respuesta.items == null || respuesta.items.isEmpty()) {
                return null;
            }

            return respuesta.items.get(0).volumeInfo;

        } catch (Exception e) {
            // si la api de google peta (500, timeout, etc.) devolvemos null
            // el servicio que llame a esto tiene que comprobar si es null
            System.out.println("Error llamando a Google Books: " + e.getMessage());
            return null;
        }
    }
}