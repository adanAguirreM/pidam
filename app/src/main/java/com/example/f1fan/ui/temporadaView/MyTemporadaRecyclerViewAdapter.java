package com.example.f1fan.ui.temporadaView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.f1fan.R;
import com.example.f1fan.modelo.DAO.DAOequipo;
import com.example.f1fan.modelo.DAO.DAOtemporada;
import com.example.f1fan.modelo.pojos.Equipo;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Temporada;
import com.example.f1fan.modelo.pojos.Usuario;
import com.example.f1fan.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.f1fan.databinding.FragmentTemporadaBinding;
import com.example.f1fan.ui.recyclerView.FullscreenPiloto;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTemporadaRecyclerViewAdapter extends RecyclerView.Adapter<MyTemporadaRecyclerViewAdapter.ViewHolder> {

    private final List<Temporada> mValues;

    private Context context;
    private FragmentManager fragmentManager;
    private DAOtemporada daoTemporada;

    public MyTemporadaRecyclerViewAdapter(List<Temporada> items, Context context, FragmentManager fragmentManager, DAOtemporada daoTemporada) {
        mValues = items;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.daoTemporada = daoTemporada;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentTemporadaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.anho.setText(String.valueOf(mValues.get(position).getAnho()));
        holder.piloto.setText("Piloto camnpeón: " + mValues.get(position).getPiloto());
        holder.equipo.setText("Equipo campeón: " + mValues.get(position).getEquipo());
        holder.carreras.setText("Nª carreras: " + mValues.get(position).getN_carreras());

        if (Usuario.getRol() == Rol.ADMIN) {
            holder.vista.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = fragmentManager.beginTransaction().setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
                    ft.replace(R.id.drawer_layout, new NuevaTemporadaFragment(fragmentManager, new DAOtemporada(), mValues.get(position)));
                    ft.addToBackStack(null);
                    ft.setReorderingAllowed(true).commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView anho;
        public final TextView piloto;
        public final TextView equipo;
        public final TextView carreras;
        public final View vista;

        public ViewHolder(FragmentTemporadaBinding binding) {
            super(binding.getRoot());
            anho = binding.anhoTemporada;
            piloto = binding.campeonTemporada;
            equipo = binding.equipoTemporada;
            carreras = binding.carrerasTemporada;
            vista = binding.temporadaContent;
        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}