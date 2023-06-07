package com.example.f1fan.modelo.DAO;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.f1fan.modelo.BD;
import com.example.f1fan.modelo.Storage;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Equipo;
import com.example.f1fan.modelo.pojos.Piloto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DAOequipo {
    private BD bd;
    private FirebaseFirestore fb;
    private DAOpiloto daoPiloto;
    private Storage storage;

    public DAOequipo() {
        bd = new BD();
        fb = bd.getDB();
        daoPiloto = new DAOpiloto();
        storage = new Storage();
    }

    public void deleteTeam(Equipo e) {
        daoPiloto.deleteFromTeam(e);
    }

    public void getEquipos() {
        fb.collection("equipos").orderBy("nombre", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Equipo e = new Equipo();
                                e.setId(document.getId());
                                e.setNombre(document.get("nombre", String.class));
                                e.setVictorias(document.get("victorias", Integer.class));
                                e.setUrl_foto(document.get("url_foto", String.class));
                                e.setAnhos_activo(document.get("anhos_activo", Integer.class));
                                e.setTeam_principal(document.get("team_principal", String.class));
                                e.setColor(document.get("color", String.class));

                                BDestatica.addEquipo(e);
                            }
                        } else {
                        }
                    }
                });
    }

    public void modificaEquipo(Equipo e, Uri img) {
        if (img == null)
            fb.collection("equipos").document(String.valueOf(e.getId()))
                    .set(e)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("piloto", "DocumentSnapshot successfully written!");
                            BDestatica.modificaEquipo(e);
                        }
                    });
        else {
            StorageReference st = storage.getReference().child("equipos/" + e.getNombre());
            st.putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storage.getReference().child("equipos/" + e.getNombre()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            e.setUrl_foto(uri.toString());
                            fb.collection("equipos").document(e.getId()).set(e);
                            BDestatica.modificaEquipo(e);
                        }
                    });
                }
            });
        }
    }

    public void add(Equipo e, Uri img) {

        StorageReference st = storage.getReference().child("equipos/" + e.getNombre());
        st.putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                storage.getReference().child("equipos/" + e.getNombre()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        e.setUrl_foto(uri.toString());
                        fb.collection("equipos").add(e).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                e.setId(task.getResult().getId());
                                BDestatica.addEquipo(e);
                            }
                        });
                    }
                });
            }
        });
    }

}
