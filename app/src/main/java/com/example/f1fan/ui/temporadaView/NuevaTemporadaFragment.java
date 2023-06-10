package com.example.f1fan.ui.temporadaView;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.f1fan.R;
import com.example.f1fan.databinding.FragmentNuevaTemporadaBinding;
import com.example.f1fan.modelo.DAO.DAOtemporada;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Temporada;

import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NuevaTemporadaFragment extends Fragment {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private FragmentManager fragmentManager;
    private DAOtemporada daoTemporada;

    private Temporada temporada;

    public NuevaTemporadaFragment(FragmentManager fragmentManager, DAOtemporada daoTemporada, Temporada temporada) {
        this.fragmentManager = fragmentManager;
        this.daoTemporada = daoTemporada;
        this.temporada = temporada;
    }

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            Activity activity = getActivity();
            if (activity != null
                    && activity.getWindow() != null) {
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

        }
    };
    private View mContentView;
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private FragmentNuevaTemporadaBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentNuevaTemporadaBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVisible = true;
        binding.textView18.setTextSize(25f);
        binding.textView18.setTextColor(Color.BLACK);

        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.frameLayout3;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        if (temporada != null) {

            binding.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoTemporada.eliminaTemporada(temporada);
                    cerrar();
                }
            });

            binding.piCamp.setText(temporada.getPiloto());
            binding.eqCamp.setText(temporada.getEquipo());
            binding.anhoEdit.setText(temporada.getAnho() + "");
            binding.numCarrerasEdit.setText(temporada.getN_carreras() + "");

            binding.numCarrerasEdit.setFocusable(false);
            binding.piCamp.setFocusable(false);
            binding.eqCamp.setFocusable(false);
            binding.anhoEdit.setFocusable(false);

            binding.button.setFocusable(false);
            binding.button3.setFocusable(true);
        } else {
            binding.button.setFocusable(true);
            binding.button3.setFocusable(false);

            binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean error = true;
                    Temporada t = new Temporada();
                    String numCarreras = binding.numCarrerasEdit.getText().toString();
                    String piloto = binding.piCamp.getText().toString();
                    String equipo = binding.eqCamp.getText().toString();
                    String anho = binding.anhoEdit.getText().toString();

                    if (numCarreras.length() == 0 || piloto.length() == 0 || equipo.length() == 0 || anho.length() == 0)
                        Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    else {
                        t.setN_carreras(Integer.parseInt(numCarreras));
                        t.setPiloto(piloto);
                        t.setEquipo(equipo);
                        t.setAnho(Integer.parseInt(anho.trim()));
                        if (t.getAnho() >= new Date().getYear() + 1900 || t.getAnho() <= 1950)
                            Toast.makeText(getContext(), "No puedes añadir una tempoara no empezada/finalizada", Toast.LENGTH_SHORT).show();
                        else {
                            boolean encontrado = false;
                            for (int i = 0; i < BDestatica.getTemporadas().size() && encontrado == false; i++) {
                                error = false;
                                if (BDestatica.getTemporadas().get(i).getAnho() == Integer.parseInt(anho)) {
                                    encontrado = true;
                                    Toast.makeText(getContext(), "Esa temporada ya existe", Toast.LENGTH_SHORT).show();
                                    error = true;
                                }
                            }
                        }

                        if (t.getN_carreras() < 10) {
                            Toast.makeText(getContext(), "Num de carreras incorrecto", Toast.LENGTH_SHORT).show();
                            error = true;
                        } else if (!error)
                            error = false;

                        if (t.getPiloto().length() < 4) {
                            Toast.makeText(getContext(), "Piloto no válido", Toast.LENGTH_SHORT).show();
                            error = true;
                        } else if (!error)
                            error = false;

                        if (t.getEquipo().length() < 4) {
                            Toast.makeText(getContext(), "Equipo no válido", Toast.LENGTH_SHORT).show();
                            error = true;
                        } else if (!error)
                            error = false;

                        if (!error) {
                            daoTemporada.addTemporada(t);
                            Toast.makeText(getContext(), "Añadiendo temporada...", Toast.LENGTH_SHORT).show();
                            cerrar();
                        }
                    }
                }
            });
        }

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar();
            }
        });
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private void cerrar() {
        fragmentManager.beginTransaction().remove(this).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            // Clear the systemUiVisibility flag
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentView = null;
        mControlsView = null;
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}