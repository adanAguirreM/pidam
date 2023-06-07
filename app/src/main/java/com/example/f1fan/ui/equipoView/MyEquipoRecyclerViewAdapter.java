package com.example.f1fan.ui.equipoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.f1fan.R;
import com.example.f1fan.modelo.DAO.DAOequipo;
import com.example.f1fan.modelo.pojos.Equipo;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.example.f1fan.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.f1fan.databinding.FragmentEquipoBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEquipoRecyclerViewAdapter extends RecyclerView.Adapter<MyEquipoRecyclerViewAdapter.ViewHolder> {

    private final List<Equipo> mValues;
    private Context context;
    private FragmentManager fragmentManager;
    private DAOequipo daoEquipo;
    public MyEquipoRecyclerViewAdapter(List<Equipo> items, Context context, FragmentManager fragmentManager, DAOequipo daoEquipo) {
        mValues = items;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.daoEquipo = daoEquipo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentEquipoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.nombre.setText(mValues.get(position).getNombre());
        holder.anhosActivo.setText("Años activo: " + String.valueOf(mValues.get(position).getAnhos_activo()));
        holder.victorias.setText("Victorias: " + String.valueOf(mValues.get(position).getVictorias()));
        holder.teamPrincipal.setText("Team Principal: " + mValues.get(position).getTeam_principal());
        if (!mValues.get(position).getColor().equals("")) {
            int color = Color.parseColor(mValues.get(position).getColor());
            holder.vista.setBackgroundColor(color);
        }

        final Bitmap[] finalD = new Bitmap[1];


        try{

            Glide.with(context)
                    .asBitmap()
                    .load(mValues.get(position).getUrl_foto())

                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            holder.imagen.setImageBitmap(resource);
                            finalD[0] = resource;

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


        }catch (Exception e){
            throw new RuntimeException(e);
        }
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
                    ft.replace(R.id.drawer_layout, new FullscreenFragmentEquipo(mValues.get(position), finalD[0], fragmentManager, daoEquipo));
                    ft.addToBackStack(null);
                    ft.setReorderingAllowed(false).commit();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nombre;
        public final TextView anhosActivo;
        public final TextView victorias;
        public final TextView teamPrincipal;
        public final ImageView imagen;
        public final View vista;


        public ViewHolder(FragmentEquipoBinding binding) {
            super(binding.getRoot());
            nombre = binding.nombreEquipo;
            anhosActivo = binding.anhos;
            victorias = binding.victoriasEquipo;
            teamPrincipal = binding.teamPrincipal;
            imagen = binding.fotoEquipo;
            vista = binding.equipoContentC;
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}