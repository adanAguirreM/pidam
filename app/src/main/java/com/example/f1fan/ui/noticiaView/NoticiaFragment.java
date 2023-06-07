package com.example.f1fan.ui.noticiaView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.f1fan.R;
import com.example.f1fan.Utils;
import com.example.f1fan.modelo.DAO.DAOnoticia;
import com.example.f1fan.modelo.DAO.DAOtemporada;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.example.f1fan.placeholder.PlaceholderContent;
import com.example.f1fan.ui.temporadaView.NuevaTemporadaFragment;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.material.snackbar.Snackbar;

/**
 * A fragment representing a list of Items.
 */
public class NoticiaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyNoticiaRecyclerViewAdapter r;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticiaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoticiaFragment newInstance(int columnCount) {
        NoticiaFragment fragment = new NoticiaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia_list, container, false);

        Utils.botonesNoticia(getActivity());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            r = new MyNoticiaRecyclerViewAdapter(new DAOnoticia(), getContext());
            recyclerView.setAdapter(r);
        }

        Toolbar t = (Toolbar) getActivity().findViewById(R.id.toolbar);
        t.setTitle("Noticias");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("::TAG", "attached fragment");
        Toolbar t = (Toolbar) getActivity().findViewById(R.id.toolbar);
        t.setTitle("Noticias");
        Utils.botonesNoticia(getActivity());
    }

}