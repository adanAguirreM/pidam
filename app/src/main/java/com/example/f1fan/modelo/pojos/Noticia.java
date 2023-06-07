package com.example.f1fan.modelo.pojos;

public class Noticia {
    private String id;
    private String titular;
    private String cuerpo;
    private String link_noticia;
    private long fech_creacion;

    public Noticia(String id, String titular, String cuerpo, String link_noticia, long fech_creacion) {
        this.id = id;
        this.titular = titular;
        this.cuerpo = cuerpo;
        this.link_noticia = link_noticia;
        this.fech_creacion = fech_creacion;
    }

    public Noticia() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getLink_noticia() {
        return link_noticia;
    }

    public void setLink_noticia(String link_noticia) {
        this.link_noticia = link_noticia;
    }

    public long getFech_creacion() {
        return fech_creacion;
    }

    public void setFech_creacion(long fech_creacion) {
        this.fech_creacion = fech_creacion;
    }
}
