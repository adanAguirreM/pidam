package com.example.f1fan.modelo.pojos;

public class Temporada {
    private String id;
    private String piloto;
    private String equipo;
    private int n_carreras;
    private int anho;

    public Temporada(String id, String piloto, String equipo, int n_carreras, int anho) {
        this.id = id;
        this.piloto = piloto;
        this.equipo = equipo;
        this.n_carreras = n_carreras;
        this.anho = anho;
    }

    public Temporada() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getN_carreras() {
        return n_carreras;
    }

    public void setN_carreras(int n_carreras) {
        this.n_carreras = n_carreras;
    }

    public String getPiloto() {
        return piloto;
    }

    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public int getAnho() {
        return anho;
    }

    public void setAnho(int anho) {
        this.anho = anho;
    }
}
