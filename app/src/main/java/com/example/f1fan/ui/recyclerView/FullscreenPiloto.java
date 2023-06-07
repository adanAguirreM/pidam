package com.example.f1fan.ui.recyclerView;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.f1fan.Utils;
import com.example.f1fan.databinding.FragmentFullscreenPilotoBinding;
import com.example.f1fan.modelo.DAO.DAOpiloto;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Equipo;
import com.example.f1fan.modelo.pojos.Piloto;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import kotlin.collections.ArrayDeque;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenPiloto extends Fragment {

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private Piloto piloto;
    private String equipo;
    private DAOpiloto daoPiloto;
    private Bitmap imagenPiloto;
    private EditText[] datos;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private FragmentManager fragmentManager;
    private Uri img;

    public FullscreenPiloto(Piloto piloto, Bitmap drawable, FragmentManager fragmentManager, DAOpiloto dao) {
        this.piloto = piloto;
        imagenPiloto = drawable;
        this.fragmentManager = fragmentManager;
        daoPiloto = dao;
    }
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

    private FragmentFullscreenPilotoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFullscreenPilotoBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean modifica = piloto != null;

        if (!modifica)
            piloto = new Piloto();

        Spinner spinner = binding.spinner;
        ArrayList<String> opciones = new ArrayList<>();

        for (Equipo e: BDestatica.getEquipos())
                opciones.add(e.getNombre());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = parent.getItemAtPosition(position).toString();
                equipo = seleccion;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mVisible = true;

        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.layoutPilotoFull;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.botonesMapa(getActivity());
                toggle();
            }
        });

        if (modifica) {
            for (int i = 0; i < BDestatica.getEquipos().size(); i++)
                if (BDestatica.getEquipos().get(i).getNombre().equals(piloto.getEquipo()))
                    spinner.setSelection(i);

            binding.nombreEdit.setText(piloto.getNombre());
            binding.apellidoEdit.setText(piloto.getApellidos());
            binding.edadEdit.setText(String.valueOf(piloto.getEdad()));
            binding.puntosEdit.setText(String.valueOf(piloto.getPuntos()));
            binding.gpEdit.setText(String.valueOf(piloto.getGp_terminados()));
            binding.victoriasEdit.setText(String.valueOf(piloto.getVictorias()));
            binding.polesEdit.setText(String.valueOf(piloto.getPole_positions()));
            binding.podiosEdit.setText(String.valueOf(piloto.getPodios()));
            binding.loadImage.setText("Eliminar");
            binding.loadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daoPiloto.eliminaPiloto(piloto);
                    cerrarFragment();
                }
            });
        } else {
            binding.loadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargarImagen();
                }
            });
        }

        if (Usuario.getRol() != Rol.ADMIN) {
            binding.nombreEdit.setFocusable(false);
            binding.apellidoEdit.setFocusable(false);
            binding.edadEdit.setFocusable(false);
            binding.spinner.setFocusable(false);
            binding.puntosEdit.setFocusable(false);
            binding.gpEdit.setFocusable(false);
            binding.victoriasEdit.setFocusable(false);
            binding.polesEdit.setFocusable(false);
            binding.podiosEdit.setFocusable(false);
            binding.loadImage.setVisibility(View.INVISIBLE);
        } else {
            binding.guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (formCheck()) {
                        piloto.setNombre(binding.nombreEdit.getText().toString());
                        piloto.setApellidos(binding.apellidoEdit.getText().toString());
                        piloto.setEdad(Integer.parseInt(binding.edadEdit.getText().toString()));
                        piloto.setPuntos(Float.parseFloat(binding.puntosEdit.getText().toString()));
                        piloto.setGp_terminados(Integer.parseInt(binding.gpEdit.getText().toString()));
                        piloto.setVictorias(Integer.parseInt(binding.victoriasEdit.getText().toString()));
                        piloto.setPole_positions(Integer.parseInt(binding.polesEdit.getText().toString()));
                        piloto.setPodios(Integer.parseInt(binding.podiosEdit.getText().toString()));
                        piloto.setEquipo(equipo);


                        if (modifica) {
                            Toast.makeText(getContext(), "Modificando piloto...", Toast.LENGTH_SHORT).show();
                            int count = 0;

                            for (Equipo e : BDestatica.getEquipos())
                                if (e.getNombre().equals(piloto.getEquipo()))
                                    for (Piloto p : BDestatica.getPilotos())
                                        if (p.getEquipo().equals(e.getNombre()) && !p.getId().equals(piloto.getId()))
                                            count++;


                            if (count < 2) {
                                daoPiloto.modificaPiloto(piloto, img);
                                cerrarFragment();
                            } else
                                Toast.makeText(getContext(), "El equipo ya contiene 2 pilotos", Toast.LENGTH_SHORT).show();

                        } else if (img != null) {
                            int count = 0;

                            for (Equipo e : BDestatica.getEquipos())
                                if (e.getNombre().equals(piloto.getEquipo()))
                                    for (Piloto p : BDestatica.getPilotos())
                                        if (p.getEquipo().equals(e.getNombre())) {
                                            count++;
                                            Toast.makeText(getContext(), p.getNombre(), Toast.LENGTH_SHORT).show();
                                        }

                            if (count < 2) {
                                daoPiloto.add(piloto, img);
                                Toast.makeText(getContext(), "Añadiendo piloto...", Toast.LENGTH_SHORT).show();
                                cerrarFragment();
                            } else
                                Toast.makeText(getContext(), "Equipo no válido", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), "Inserta una imagen", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.imageView2.setImageBitmap(imagenPiloto);

        binding.atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarFragment();
            }
        });


    }

    private boolean formCheck() {
        boolean result = true;

        if (binding.nombreEdit.getText().toString().equals(""))
            return false;

        if (piloto.getEquipo() == "")
            return false;

        if (binding.apellidoEdit.getText().toString().equals(""))
            return false;

        if (binding.edadEdit.getText().toString().equals(""))
            return false;

        if (binding.gpEdit.getText().toString().equals(""))
            return false;

        if (binding.podiosEdit.getText().toString().equals(""))
            return false;

        if (binding.victoriasEdit.getText().toString().equals(""))
            return false;

        if (binding.puntosEdit.getText().toString().equals(""))
            return false;

        if (binding.polesEdit.getText().toString().equals(""))
            return false;

        return result;
    }

    private void cerrarFragment() {
        fragmentManager.beginTransaction().remove(this).commit();
    }

    private void cargarImagen() {
        final int RESULT_LOAD_IMG = 101;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        img = data.getData();
        if (img != null) {
            Utils.botonesMapa(getActivity());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), img);
                binding.imageView2.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

        Utils.botonesMapa(getActivity());
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