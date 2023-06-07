package com.example.f1fan.modelo.pojos;

public class Equipo {
    private String id;
    private String nombre;
    private String team_principal;
    private int victorias;
    private int anhos_activo;
    private String url_foto;

    private String color;

    public Equipo(String id, String nombre, String team_principal, int victorias, int anhos_activo, String url_foto) {
        this.id = id;
        this.nombre = nombre;
        this.team_principal = team_principal;
        this.victorias = victorias;
        this.anhos_activo = anhos_activo;
        this.url_foto = url_foto;
    }

    public Equipo() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTeam_principal() {
        return team_principal;
    }

    public void setTeam_principal(String team_principal) {
        this.team_principal = team_principal;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getAnhos_activo() {
        return anhos_activo;
    }

    public void setAnhos_activo(int anhos_activo) {
        this.anhos_activo = anhos_activo;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
