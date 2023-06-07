package com.example.f1fan;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import com.example.f1fan.modelo.DAO.DAORanking;
import com.example.f1fan.modelo.DAO.DAOcircuito;
import com.example.f1fan.modelo.DAO.DAOequipo;
import com.example.f1fan.modelo.DAO.DAOnoticia;
import com.example.f1fan.modelo.DAO.DAOpiloto;
import com.example.f1fan.modelo.DAO.DAOtemporada;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.f1fan.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Usuario user = new Usuario();

    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (user.getRol() != Rol.ANONIMO && !BDestatica.comprobarDatos()) {
            DAOtemporada daOtemporada = new DAOtemporada();
            DAOequipo daOequipo = new DAOequipo();
            DAOpiloto daOpiloto = new DAOpiloto();
            daOpiloto.getPilotos();
            daOequipo.getEquipos();
            daOtemporada.getTemporadas();
            new DAORanking().getRanking();
            new DAOcircuito().getCircuitos();
        }

        if (BDestatica.getNoticias().size() == 0)
            new DAOnoticia().getNoticias();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.appBarMain.toolbar);


        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Usuario.getRol() == Rol.ANONIMO)
                    Snackbar.make(view, "Para un acceso completo registrese en la app", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (Usuario.getRol() == Rol.REGISTRADO)
                    Snackbar.make(view, "Todos los días nuevas noticias en F1 Fan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
        findViewById(R.id.add).setVisibility(View.INVISIBLE);

        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        if (user.getRol() == Rol.ANONIMO) {
            navigationView.getMenu().getItem(1).setEnabled(false);
            navigationView.getMenu().getItem(2).setEnabled(false);
            navigationView.getMenu().getItem(4).setEnabled(false);
        }


        mAppBarConfiguration = new AppBarConfiguration.Builder(
           R.id.inicio, R.id.slideshowFragment, R.id.slideshowFragment2, R.id.noticiaFragment, R.id.mapsFragment)
           .setOpenableLayout(drawer)
           .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Inicio");



        notificaciones();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (Usuario.getUsuario().isAnonymous())
                    Usuario.getUsuario().delete();

                Usuario.setUsuario(null);
                user.setEmail("");
                user.setPasswd("");
                user.setRol(null);
                finish();
                onBackPressed();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void notificaciones() {
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d("::TAG", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("::TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d("::TAG", token.toString());
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        user.setEmail("");
        user.setPasswd("");
        user.setRol(null);
    }
}