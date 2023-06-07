package com.example.f1fan.modelo.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.f1fan.modelo.BD;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.PilotoRanking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DAORanking {
    private BD bd;
    private FirebaseFirestore fb;

    public DAORanking() {
        bd = new BD();
        fb = bd.getDB();
    }

    public void getRanking() {

        fb.collection("ranking").orderBy("ptos", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ranking", BDestatica.getPilotosRanking().size() + " - " + document.getId() + " => " + document.getData() );
                                PilotoRanking r = new PilotoRanking();
                                r.setNombre(document.get("nombre", String.class));
                                r.setPuntos(document.get("ptos", Float.class));
                                r.setId(document.getId());

                                BDestatica.addPilotoRanking(r);
                            }
                        } else {
                            Log.d("temporada", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void modificaRanking(PilotoRanking p) {
        fb.collection("ranking").document(p.getId()).set(p).addOnSuccessListener(new OnSuccessListener() {

                    @Override
                    public void onSuccess(Object o) {
                        Log.d("temporada", "DocumentSnapshot successfully written!");
                        BDestatica.modificaRanking(p);
                    }
                });
    }
}
