package com.example.f1fan.modelo.DAO;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.f1fan.modelo.BD;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;

public class DAOusuario {
    private BD bd;
    private FirebaseFirestore fb;

    public DAOusuario() {
        bd = new BD();
        fb = bd.getDB();
    }

    public void usuarioExiste(Usuario usuario) {
        fb.collection("usuarios").whereEqualTo("email", usuario.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        } else {
                            Log.d("usuarios", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getRol(Usuario u) {
        fb.collection("usuarios").whereEqualTo("email", u.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0)
                                registrarUsuario(u);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("usuarios", document.getId() + " => " + document.getData());
                                String rol = document.get("rol", String.class);
                                if (rol.equalsIgnoreCase(Rol.REGISTRADO.toString()))
                                    u.setRol(Rol.REGISTRADO);
                                else
                                    u.setRol(Rol.ADMIN);
                            }
                        } else {
                            Log.d("usuarios", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void registrarUsuario(Usuario u) {
        Map<String, String> user = new HashMap<>();
        user.put("email", u.getEmail());
        user.put("rol", "REGISTRADO");

        fb.collection("usuarios").add(user);
    }
}
