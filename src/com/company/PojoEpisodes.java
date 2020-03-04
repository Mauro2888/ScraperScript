package com.company;

public class PojoEpisodes {
    String numero;
    String url;

    public PojoEpisodes(String numero, String url) {
        this.numero = numero;
        this.url = url;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
