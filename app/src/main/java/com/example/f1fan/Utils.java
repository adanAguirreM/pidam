package com.example.f1fan;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.f1fan.modelo.DAO.DAOequipo;
import com.example.f1fan.modelo.DAO.DAOnoticia;
import com.example.f1fan.modelo.DAO.DAOpiloto;
import com.example.f1fan.modelo.DAO.DAOtemporada;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Equipo;
import com.example.f1fan.modelo.pojos.Piloto;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.example.f1fan.ui.equipoView.FullscreenFragmentEquipo;
import com.example.f1fan.ui.noticiaView.NuevaNoticiaFragment;
import com.example.f1fan.ui.recyclerView.FullscreenPiloto;
import com.example.f1fan.ui.temporadaView.NuevaTemporadaFragment;
import com.google.android.material.snackbar.Snackbar;

public class Utils {
    public static void botones(FragmentActivity a) {
        a.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        if (a.findViewById(R.id.add).isEnabled())
            a.findViewById(R.id.add).setVisibility(View.INVISIBLE);
        a.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Usuario.getRol() == Rol.ANONIMO)
                    Snackbar.make(view, "Para un acceso completo registrese en la app", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(view, "Todos los días nuevas noticias en F1 Fan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    public static void botonesEquipo(FragmentActivity a) {
        a.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        a.findViewById(R.id.add).setVisibility(View.INVISIBLE);

        if (Usuario.getRol() == Rol.ADMIN) {
            a.findViewById(R.id.add).setVisibility(View.VISIBLE);
            a.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BDestatica.getEquipos().size() >= 11)
                        Toast.makeText(a.getApplicationContext(), "No se pueden añadir más equipos (max 11 equipos)", Toast.LENGTH_SHORT).show();
                    else {
                        FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                        );
                        ft.replace(R.id.nav_host_fragment_content_main, new FullscreenFragmentEquipo(null, null, a.getSupportFragmentManager(), new DAOequipo()));
                        ft.addToBackStack(null);
                        ft.setReorderingAllowed(true).commit();
                    }
                }
            });
        }
        a.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Usuario.getRol() == Rol.ANONIMO)
                    Snackbar.make(view, "Para un acceso completo registrese en la app", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(view, "Todos los días nuevas noticias en F1 Fan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    public static void botonesPiloto(FragmentActivity a) {
        a.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        if (a.findViewById(R.id.add).isEnabled())
            a.findViewById(R.id.add).setVisibility(View.INVISIBLE);

        if (Usuario.getRol() == Rol.ADMIN) {
            a.findViewById(R.id.add).setVisibility(View.VISIBLE);
            a.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cont = 0;
                    for(Equipo e: BDestatica.getEquipos())
                        for(Piloto p: BDestatica.getPilotos())
                            if (p.getEquipo().equals(e.getNombre()))
                                cont++;
                    if (cont >= (BDestatica.getEquipos().size() * 2)) {
                        Toast.makeText(a.getApplicationContext(), "No se pueden añadir más pilotos (max 2 por equipo)", Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                        );
                        ft.replace(R.id.nav_host_fragment_content_main, new FullscreenPiloto(null, null, a.getSupportFragmentManager(), new DAOpiloto()));
                        ft.addToBackStack(null);
                        ft.setReorderingAllowed(true).commit();
                    }
                }
            });
        }

        a.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Para ver toda la info pulsa sobre un piloto", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static void botonesHistorico(FragmentActivity a) {
        a.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        if (Usuario.getRol() == Rol.ADMIN) {
            a.findViewById(R.id.add).setVisibility(View.VISIBLE);
            a.findViewById(R.id.add).setEnabled(true);
            a.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
                    ft.replace(R.id.drawer_layout, new NuevaTemporadaFragment(a.getSupportFragmentManager(), new DAOtemporada(), null));
                    ft.addToBackStack(null);
                    ft.setReorderingAllowed(true).commit();
                }
            });
        }

        a.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Usuario.getRol() == Rol.ANONIMO)
                    Snackbar.make(view, "Para un acceso completo registrese en la app", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(view, "Todos los días nuevas noticias en F1 Fan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    public static void botonesMapa(FragmentActivity a) {
        a.findViewById(R.id.add).setVisibility(View.INVISIBLE);
        a.findViewById(R.id.fab).setVisibility(View.INVISIBLE);
    }

    public static void botonesNoticia(FragmentActivity a) {
        a.findViewById(R.id.fab).setVisibility(View.VISIBLE);
        if (Usuario.getRol() == Rol.ADMIN) {
            a.findViewById(R.id.add).setVisibility(View.VISIBLE);
            a.findViewById(R.id.add).setEnabled(true);
            a.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = a.getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
                    ft.replace(R.id.nav_host_fragment_content_main, new NuevaNoticiaFragment(a.getSupportFragmentManager(), new DAOnoticia()));
                    ft.addToBackStack(null);
                    ft.setReorderingAllowed(true).commit();
                }
            });
        }

        a.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Usuario.getRol() == Rol.ANONIMO)
                    Snackbar.make(view, "Para un acceso completo registrese en la app", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                    Snackbar.make(view, "Todos los días nuevas noticias en F1 Fan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }
}
