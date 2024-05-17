package com.ifp.gestionaudios;

public class Audio {
    private int id;
    private String titulo;
    private String url;

    // Constructor
    public Audio(int id, String titulo, String url) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
    }

    // Método para obtener el título
    public String getTitulo() {
        return titulo;
    }

    // Método para obtener la URL
    public String getUrl() {
        return url;
    }

}
