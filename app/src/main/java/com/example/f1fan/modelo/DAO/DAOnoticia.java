package com.example.f1fan.modelo.DAO;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.f1fan.modelo.BD;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Noticia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Date;

public class DAOnoticia {

    private BD bd;
    private FirebaseFirestore fb;
    private long fecha = (int) ((new Date().getTime() / 86400000));

    public DAOnoticia() {
        bd = new BD();
        fb = bd.getDB();
    }

    public void getNoticias() {
        fb.collection("noticias").whereEqualTo("fech_creacion", fecha)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() < 1) {
                                fecha--;
                                getNoticias();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Noticia n = new Noticia();

                                n.setId(document.getId());
                                n.setTitular(document.get("titular", String.class));
                                n.setCuerpo(document.get("cuerpo", String.class));
                                n.setLink_noticia(document.get("link_noticia", String.class));
                                n.setFech_creacion(document.get("fech_creacion", Integer.class));

                                BDestatica.addNoticia(n);
                            }
                        } else {
                            Log.d("piloto", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addNoticia(Noticia n) {
        BDestatica.addNoticia(n);
        fb.collection("noticias").add(n);

    }
}
