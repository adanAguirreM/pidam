package com.example.f1fan.modelo.pojos;

public class Piloto {
    private String id;
    private String equipo;
    private String nombre;
    private String apellidos;
    private int edad;
    private int podios;
    private int pole_positions;
    private int victorias;
    private float puntos;
    private int gp_terminados;
    private String url_foto;

    public Piloto(String id, String equipo, String nombre, String apellidos, int edad, int podios, int pole_positions, int victorias, float puntos, int gp_terminados, String url_foto) {
        this.id = id;
        this.equipo = equipo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.podios = podios;
        this.pole_positions = pole_positions;
        this.victorias = victorias;
        this.puntos = puntos;
        this.gp_terminados = gp_terminados;
        this.url_foto = url_foto;
    }

    public Piloto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPodios() {
        return podios;
    }

    public void setPodios(int podios) {
        this.podios = podios;
    }

    public int getPole_positions() {
        return pole_positions;
    }

    public void setPole_positions(int pole_positions) {
        this.pole_positions = pole_positions;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public float getPuntos() {
        return puntos;
    }

    public void setPuntos(float puntos) {
        this.puntos = puntos;
    }

    public int getGp_terminados() {
        return gp_terminados;
    }

    public void setGp_terminados(int gp_terminados) {
        this.gp_terminados = gp_terminados;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }
}
