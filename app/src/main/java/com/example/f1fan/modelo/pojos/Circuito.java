package com.example.f1fan.modelo.pojos;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class Circuito {
    private String id;
    private String nombre;
    private float km_totales;
    private String hotlap;
    private String victoria;
    private String url_foto;
    private int orden;
    private double lat;
    private double lon;

    public Circuito(String id, String nombre, float km_totales, String hotlap, String victoria, String url_foto, int orden, double lat, double lon) {
        this.id = id;
        this.nombre = nombre;
        this.km_totales = km_totales;
        this.hotlap = hotlap;
        this.victoria = victoria;
        this.url_foto = url_foto;
        this.orden = orden;
        this.lat = lat;
        this.lon = lon;
    }

    public Circuito() {
    }

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

    public float getKm_totales() {
        return km_totales;
    }

    public void setKm_totales(float km_totales) {
        this.km_totales = km_totales;
    }

    public String getHotlap() {
        return hotlap;
    }

    public void setHotlap(String hotlap) {
        this.hotlap = hotlap;
    }

    public String getVictoria() {
        return victoria;
    }

    public void setVictoria(String victoria) {
        this.victoria = victoria;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean result = false;
        Circuito c = (Circuito) obj;
        if (c.getLat() == this.getLat() && c.getLon() == this.getLon())
            result = true;

        return result;
    }
}
