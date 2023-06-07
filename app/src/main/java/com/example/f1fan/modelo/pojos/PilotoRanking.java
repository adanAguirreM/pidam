package com.example.f1fan.modelo.pojos;

public class PilotoRanking {
    private String nombre;
    private float puntos;
    private String id;

    public PilotoRanking(String nombre, float puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PilotoRanking() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPuntos() {
        return puntos;
    }

    public void setPuntos(float puntos) {
        this.puntos = puntos;
    }
}
