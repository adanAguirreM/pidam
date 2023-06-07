package com.example.f1fan.modelo.DAO;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.f1fan.modelo.BD;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Circuito;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DAOcircuito {
    private BD bd;
    private FirebaseFirestore fb;

    public DAOcircuito() {
        bd = new BD();
        fb = bd.getDB();
    }

    public void getCircuitos() {
        fb.collection("circuitos").orderBy("orden", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("circuito", document.getId() + " => " + document.getData());
                                Circuito c = new Circuito();
                                c.setId(document.getId());
                                c.setNombre(document.get("nombre", String.class));
                                c.setHotlap(document.get("hotlap", String.class));
                                c.setLat(document.get("lat", Double.class));
                                c.setLon(document.get("lon", Double.class));
                                c.setKm_totales(document.get("km", Float.class));
                                c.setOrden(document.get("orden", Integer.class));
                                c.setVictoria(document.get("vic", String.class));
                                c.setUrl_foto(document.get("url", String.class));

                                BDestatica.addCircuito(c);
                            }
                        } else {
                            Log.d("::TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
