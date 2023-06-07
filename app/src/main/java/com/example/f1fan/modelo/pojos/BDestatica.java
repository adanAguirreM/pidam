package com.example.f1fan.modelo.pojos;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BDestatica {
    private static ArrayList<Circuito> circuitos = new ArrayList<>();
    private static ArrayList<Equipo> equipos = new ArrayList<>();
    private static ArrayList<Noticia> noticias = new ArrayList<>();
    private static ArrayList<Piloto> pilotos = new ArrayList<>();
    private static ArrayList<Temporada> temporadas = new ArrayList<>();
    private static ArrayList<PilotoRanking> pilotosRanking = new ArrayList<>();

    public static ArrayList<PilotoRanking> getPilotosRanking() {
        return pilotosRanking;
    }

    public static void addPilotoRanking(PilotoRanking p) {
        pilotosRanking.add(p);
    }

    public static ArrayList<Piloto> getPilotos() {
        return pilotos;
    }

    public static void addPiloto(Piloto piloto) {
        pilotos.add(piloto);
    }

    public static void addCircuito(Circuito c) {
        Log.d("::TAG", "" + c.getNombre());
        circuitos.add(0, c);
    }

    public static ArrayList<Circuito> getCircuitos() {
        return circuitos;
    }

    public static void deleteTeam(Equipo e) {
        List<Piloto> pilotosCopia = (List<Piloto>) pilotos.clone();
        equipos.remove(e);
        for (int i = 0; i < pilotosCopia.size(); i++) {
            if (pilotos.get(i).getEquipo().equalsIgnoreCase(e.getNombre()))
                pilotos.remove(pilotos.get(i));
        }
    }
    public static void modificaPiloto(Piloto pilotoNuevo) {
        for (int index = 0; index < pilotos.size() - 1; index++) {
            if (pilotos.get(index).getId() == pilotoNuevo.getId()){
                pilotos.remove(index);
                pilotos.add(pilotoNuevo);
            }
        }
    }

    public static void modificaEquipo(Equipo equipoNuevo) {
        for (int index = 0; index < equipos.size() - 1; index++) {
            if (equipos.get(index).getId() == equipoNuevo.getId()){
                equipos.remove(index);
                equipos.add(equipoNuevo);
            }
        }
    }

    public static void addEquipo(Equipo e) {
        equipos.add(e);
    }

    public static ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public static void addTemporada(Temporada t) {
        temporadas.add(t);
    }

    public static ArrayList<Temporada> getTemporadas() {
        return temporadas;
    }

    public static void modificaRanking(PilotoRanking p) {
        for (PilotoRanking r:pilotosRanking) {
            if (r.getId().equals(p.getId()))
                r = p;
        }
    }

    public static void addNoticia(Noticia n) {
        noticias.add(n);
    }

    public static ArrayList<Noticia> getNoticias() {
        return noticias;
    }

    public static boolean comprobarDatos() {
        boolean datos = true;

        if (pilotos.size() == 0 || equipos.size() == 0 ||temporadas.size() == 0
        || pilotosRanking.size() == 0 || circuitos.size() == 0)
            datos = false;


        return datos;
    }

    public static void deletePilot(Piloto p) {
        pilotos.remove(p);
    }
}