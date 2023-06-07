package com.example.f1fan.modelo;

import com.google.firebase.firestore.FirebaseFirestore;

public class BD {
    private final static FirebaseFirestore fb = FirebaseFirestore.getInstance();

    public BD() {
    }

    public FirebaseFirestore getDB() {
        return fb;
    }



}
