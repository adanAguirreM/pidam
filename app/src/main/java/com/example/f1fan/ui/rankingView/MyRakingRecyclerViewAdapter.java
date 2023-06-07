package com.example.f1fan.ui.rankingView;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.f1fan.modelo.pojos.PilotoRanking;
import com.example.f1fan.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.f1fan.databinding.FragmentRankingBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRakingRecyclerViewAdapter extends RecyclerView.Adapter<MyRakingRecyclerViewAdapter.ViewHolder> {

    private final List<PilotoRanking> mValues;

    public MyRakingRecyclerViewAdapter(List<PilotoRanking> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRankingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.puntos.setText(String.valueOf(mValues.get(position).getPuntos()));
        holder.piloto.setText(mValues.get(position).getNombre());
        holder.puesto.setText(String.valueOf(position + 1));

        holder.vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView piloto;
        public final TextView puesto;
        public final TextView puntos;
        public final View vista;

        public ViewHolder(FragmentRankingBinding binding) {
            super(binding.getRoot());
            piloto = binding.pilotoRanking;
            puesto = binding.puesto;
            puntos = binding.ptosRanking;
            vista = binding.rankingContent;

        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}