package com.example.f1fan.ui.noticiaView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.f1fan.R;
import com.example.f1fan.databinding.FragmentNuevaNoticiaBinding;
import com.example.f1fan.modelo.DAO.DAOnoticia;
import com.example.f1fan.modelo.pojos.Noticia;
import java.net.URL;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NuevaNoticiaFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager fragmentManager;
    private DAOnoticia daoNoticia;

    public NuevaNoticiaFragment(FragmentManager fragmentManager, DAOnoticia daoNoticia) {
        this.fragmentManager = fragmentManager;
        this.daoNoticia = daoNoticia;
    }

    public NuevaNoticiaFragment() {

    }

    public static NuevaNoticiaFragment newInstance(String param1, String param2) {
       NuevaNoticiaFragment fragment = new NuevaNoticiaFragment();
       Bundle args = new Bundle();
       args.putString(ARG_PARAM1, param1);
       args.putString(ARG_PARAM2, param2);
       fragment.setArguments(args);
       return fragment;
   }
    private FragmentNuevaNoticiaBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_noticia, container, false);

        TextView tv = view.findViewById(R.id.textView18);
        tv.setTextSize(25f);
        tv.setTextColor(Color.BLACK);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = true;
                Noticia n = new Noticia();
                view.findViewById(R.id.titular);
                n.setTitular(((EditText)view.findViewById(R.id.titular)).getText().toString());
                n.setLink_noticia(((EditText)view.findViewById(R.id.enlace)).getText().toString());
                n.setCuerpo(((EditText)view.findViewById(R.id.cuerpo)).getText().toString());

                if (n.getTitular().equals("") || n.getCuerpo().equals("") || n.getLink_noticia().equals(""))
                    Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        Date d = new Date();
                        new URL(n.getLink_noticia()).toURI();
                        n.setFech_creacion((long)(d.getTime() / 86400000));
                        daoNoticia.addNoticia(n);
                        cerrar();
                    } catch (Exception e) {
                        Log.d("::TAG", "" + e.getMessage());
                        Toast.makeText(getContext(), "URL no válida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar();
            }
        });
        return  view;

    }

    private void cerrar() {
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, new NoticiaFragment()).commit();
    }
}