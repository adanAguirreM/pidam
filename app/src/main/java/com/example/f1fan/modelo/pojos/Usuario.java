package com.example.f1fan.modelo.pojos;

import com.google.firebase.auth.FirebaseUser;

public class Usuario {
    private static FirebaseUser usuario;
    private static String email;
    private static String passwd;
    private static Rol rol;

    public static FirebaseUser getUsuario() {
        return usuario;
    }

    public static void setUsuario(FirebaseUser usuario) {
        Usuario.usuario = usuario;
    }

    public static Rol getRol() {
        return rol;
    }

    public  void setRol(Rol rol) {
        this.rol = rol;
    }

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
