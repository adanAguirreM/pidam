package com.example.f1fan.modelo.DAO;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.f1fan.modelo.BD;
import com.example.f1fan.modelo.Storage;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Equipo;
import com.example.f1fan.modelo.pojos.Piloto;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class DAOpiloto {
    private BD bd;
    private FirebaseFirestore fb;
    private Storage storage;

    public DAOpiloto() {
        bd = new BD();
        fb = bd.getDB();
        storage = new Storage();
    }

    public void deleteFromTeam(Equipo e) {
        try {
            Log.d("::TAG", "entra borrado " + fb.collection("pilotos").whereEqualTo("equipo", e.getNombre()));

            fb.collection("pilotos").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.get("equipo", String.class).equalsIgnoreCase(e.getNombre())) {
                                        fb.collection("pilotos").document(document.getId()).delete();
                                        storage.getReference().child("pilotos/" + document.get("utl_foto", Storage.class)).delete();
                                    }
                                }
                                fb.collection("equipos").document(e.getId()).delete();
                                BDestatica.deleteTeam(e);
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.d("::TAG", "error: " + ex.getMessage());
        }
    }
    public void getPilotos() {
        fb.collection("pilotos").orderBy("equipo", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Piloto p = new Piloto();
                                p.setId(document.getId());
                                p.setNombre(document.get("nombre", String.class));
                                p.setApellidos(document.get("apellidos", String.class));
                                p.setEdad(document.get("edad", Integer.class));
                                p.setEquipo(document.get("equipo", String.class));
                                p.setGp_terminados(document.get("gp_terminados", Integer.class));
                                p.setVictorias(document.get("victorias", Integer.class));
                                p.setPole_positions(document.get("pole_positions", Integer.class));
                                p.setPuntos(document.get("puntos", Float.class));
                                p.setPodios(document.get("podios", Integer.class));
                                p.setUrl_foto(document.get("url_foto", String.class));

                                BDestatica.addPiloto(p);
                            }
                        } else {
                            Log.d("piloto", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void modificaPiloto(Piloto pilotoNuevo, Uri img) {
        if (img == null)
            fb.collection("pilotos").document(String.valueOf(pilotoNuevo.getId()))
                .set(pilotoNuevo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("piloto", "DocumentSnapshot successfully written!");
                        BDestatica.modificaPiloto(pilotoNuevo);
                    }
                });
        else {
            StorageReference st = storage.getReference().child("pilotos/" + pilotoNuevo.getNombre());
            st.putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storage.getReference().child("pilotos/" + pilotoNuevo.getNombre()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pilotoNuevo.setUrl_foto(uri.toString());
                            fb.collection("pilotos").document(pilotoNuevo.getId()).set(pilotoNuevo);
                            BDestatica.modificaPiloto(pilotoNuevo);
                        }
                    });
                }
            });
        }
    }

    public void add(Piloto p, Uri img) {

        StorageReference st = storage.getReference().child("pilotos/" + p.getNombre());
        st.putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                storage.getReference().child("pilotos/" + p.getNombre()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        p.setUrl_foto(uri.toString());
                        fb.collection("pilotos").add(p);
                        fb.collection("pilotos").whereEqualTo("url_foto", p.getUrl_foto()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot document: task.getResult()) {
                                    p.setId(document.getId());
                                    BDestatica.addPiloto(p);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void eliminaPiloto(Piloto p) {
        fb.collection("pilotos").document(p.getId()).delete();
        BDestatica.deletePilot(p);

    }
}
