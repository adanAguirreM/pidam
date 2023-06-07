package com.example.f1fan.ui.noticiaView;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.f1fan.modelo.DAO.DAOnoticia;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Noticia;
import com.example.f1fan.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.f1fan.databinding.FragmentNoticiaBinding;
import java.util.List;

public class MyNoticiaRecyclerViewAdapter extends RecyclerView.Adapter<MyNoticiaRecyclerViewAdapter.ViewHolder> {

    private final List<Noticia> mValues;
    private DAOnoticia daoNoticia;
    private Context context;

    public MyNoticiaRecyclerViewAdapter(DAOnoticia d, Context context) {
        daoNoticia = d;
        mValues = BDestatica.getNoticias();
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentNoticiaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titular.setText(mValues.get(position).getTitular());
        holder.cuerpo.setText(mValues.get(position).getCuerpo());

        holder.vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mValues.get(position).getLink_noticia()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View vista;
        public PlaceholderItem mItem;
        public TextView titular;
        public TextView cuerpo;

        public ViewHolder(FragmentNoticiaBinding binding) {
            super(binding.getRoot());
            titular = binding.titular;
            cuerpo = binding.cuerpo;
            vista = binding.noticiaContent;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}