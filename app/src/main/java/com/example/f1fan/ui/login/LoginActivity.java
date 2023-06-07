package com.example.f1fan.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.f1fan.MainActivity;
import com.example.f1fan.R;
import com.example.f1fan.modelo.DAO.DAOusuario;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;
import com.example.f1fan.databinding.ActivityLoginBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;
    private String email;
    private String passwd;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
            "(?=.*[@#$%^&+=])" +     // at least 1 special character
            "(?=\\S+$)" +                     // no white spaces
            ".{8,}" +                              // at least 4 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.anonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anonimo();
            }
        });

        binding.iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        binding.textView17.setTextSize(40);
        binding.resetPassword.setClickable(true);
        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.editTextText.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mAuth.sendPasswordResetEmail(email);
                    Snackbar.make(v, "Email de recuperación enviado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else
                    Snackbar.make(v, "Introduce el email", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

    }

    public void logIn() {
        email = binding.editTextText.getText().toString();
        passwd = binding.editTextTextPassword.getText().toString();
        final boolean[] navegar = {false};

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PASSWORD_PATTERN.matcher(passwd).matches()){
            Toast.makeText(this, "Contraseña no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                Usuario u = new Usuario();
                                u.setUsuario(user);
                                u.setEmail(email);
                                u.setPasswd(passwd);
                                DAOusuario d = new DAOusuario();
                                d.getRol(u);

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                onActivityResult(0, 0, null);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this, "Verifique su e-mail", Toast.LENGTH_SHORT).show();
                                user.sendEmailVerification();
                            }

                        } else {
                            if (task.getException().getMessage() == "The password is invalid or the user does not have a password.")
                                Toast.makeText(LoginActivity.this, "Contraseña o email incorrecto", Toast.LENGTH_SHORT).show();
                            else {
                                confirmarPasswd();

                            }
                        }
                        borrarCampos();
                    }
                });


    }

    public void borrarCampos() {
        binding.editTextTextPassword.setText("");
        binding.editTextText.setText("");
    }

    public void anonimo() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Usuario u = new Usuario();
                            u.setRol(Rol.ANONIMO);
                            u.setUsuario(user);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }
                });
    }

    private void confirmarPasswd() {
        // Inflar el diseño del diálogo
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_verification, null);

        // Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Verificación de Contraseña");

        // Obtener referencias a los elementos del diseño del diálogo
        EditText passwordEditText = dialogView.findViewById(R.id.passwordEditText);

        AlertDialog dialog = builder.create();

        // Configurar el botón de verificación
        builder.setPositiveButton("Verificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = passwordEditText.getText().toString();

                // Realizar la verificación de la contraseña
                if (verifyPassword(password)) {
                    // Contraseña válida, realizar alguna acción
                    Toast.makeText(LoginActivity.this, "Contraseña válida", Toast.LENGTH_SHORT).show();
                    registrarUsuario();
                } else {
                    // Contraseña inválida, mostrar un mensaje de error
                    Toast.makeText(LoginActivity.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Mostrar el diálogo
        dialog = builder.create();
        dialog.show();
    }

    private void registrarUsuario() {
        mAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Usuario.setUsuario(mAuth.getCurrentUser());
                Usuario.getUsuario().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "E-mail de verificación enviado", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("::TAG", task.getException().getMessage());
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean verifyPassword(String password) {
        // Realiza la lógica de verificación de la contraseña
        // Devuelve true si la contraseña es válida, false en caso contrario
        // Aquí puedes implementar tu propia lógica de verificación de contraseña
        return password.equals(passwd);
    }


}