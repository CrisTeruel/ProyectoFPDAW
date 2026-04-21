package com.libreria.libreria_teruel_ayala.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// esta clase mapea la respuesta JSON de google books
// JsonIgnoreProperties ignora los campos que no nos interesan
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponse {

    public List<Item> items;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public VolumeInfo volumeInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeInfo {
        public String title;
        public List<String> authors;
        public String publisher;
        public String publishedDate;
        public String description;
        public ImageLinks imageLinks;
        public List<String> categories;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageLinks {
        public String thumbnail;
    }
}