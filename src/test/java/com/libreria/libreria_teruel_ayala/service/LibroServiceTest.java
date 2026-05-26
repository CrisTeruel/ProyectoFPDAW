package com.libreria.libreria_teruel_ayala.service;

import com.libreria.libreria_teruel_ayala.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;
    @Mock
    private AutorRepository autorRepository;
    @Mock
    private EditorialRepository editorialRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private GoogleBooksService googleBooksService;

    @InjectMocks
    private LibroService libroService;

    @BeforeEach
    void setup() {
        // arranca todos los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void siElLibroYaExisteLanzaExcepcion() {
        // simulamos que el libro ya esta en la bbdd
        when(libroRepository.existsById("9780547928227")).thenReturn(true);

        // tiene que petar
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            libroService.añadirLibro("9780547928227");
        });

        assertEquals("El libro ya existe en la libreria", ex.getMessage());
    }

    @Test
    void siGoogleNoEncuentraLibroLanzaExcepcion() {
        // el libro no existe en bbdd
        when(libroRepository.existsById("9780547928227")).thenReturn(false);
        // google devuelve null (no encontrado o error)
        when(googleBooksService.buscarPorIsbn(anyString())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            libroService.añadirLibro("9780547928227");
        });

        assertEquals("No se ha encontrado el libro en Google Books", ex.getMessage());
    }

    @Test
    void siLibroNoExisteAlBorrarLanzaExcepcion() {
        when(libroRepository.existsById("9780547928227")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            libroService.borrarLibro("9780547928227");
        });

        assertEquals("El libro no existe en la libreria", ex.getMessage());
    }
}