package com.example.f1fan.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.f1fan.Utils;
import com.example.f1fan.modelo.pojos.BDestatica;
import com.example.f1fan.modelo.pojos.Circuito;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Utils.botonesMapa(getActivity());
            ArrayList<Circuito> c = BDestatica.getCircuitos();
            int verde = 0;
            int azul = 0;
            int alpha = 255;

            for (int i = 0; i < c.size(); i++) {
                LatLng l = new LatLng(c.get(i).getLat(), c.get(i).getLon());

                if (i < c.size() -1) {
                    LatLng l2 = new LatLng(c.get(i + 1).getLat(), c.get(i + 1).getLon());
                    googleMap.addPolyline(new PolylineOptions().add(l, l2).width(10).color(Color.argb(alpha, 255, verde, azul)).jointType(JointType.ROUND));
                }

                googleMap.addMarker(new MarkerOptions().position(l).title(c.get(i).getNombre()));
                verde += 10;
                azul += 15;
                alpha -= 5;
                Log.d("::TAG", "a::: " + c.get(i).getNombre());
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    LatLng latLng = marker.getPosition();

                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                    View bottomSheetView = LayoutInflater.from(getContext())
                            .inflate(R.layout.modal_view,
                                    getActivity().findViewById(R.id.modal));

                    Circuito c = new Circuito();
                    c.setLat(latLng.latitude);
                    c.setLon(latLng.longitude);

                    for (Circuito cir : BDestatica.getCircuitos()) {
                        if (cir.equals(c)) {
                            ((TextView) bottomSheetView.findViewById(R.id.hotlap)).setText(cir.getHotlap());
                            ((TextView) bottomSheetView.findViewById(R.id.ultimaVictoria)).setText(cir.getVictoria());
                            ((TextView) bottomSheetView.findViewById(R.id.kmTotales)).setText(String.valueOf(cir.getKm_totales()));
                            ((TextView) bottomSheetView.findViewById(R.id.nombreCircuito)).setText(cir.getNombre());

                            try{
                                Glide.with(getContext())
                                        .asBitmap()
                                        .load(cir.getUrl_foto())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                ((ImageView) bottomSheetView.findViewById(R.id.imageView4)).setImageBitmap(resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });


                            }catch (Exception e){
                                throw new RuntimeException(e);
                            }


                            bottomSheetDialog.setContentView(bottomSheetView);
                            bottomSheetDialog.show();
                            return true;
                        }
                    }

                    return true;
                }
            });
        }


    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar t = getActivity().findViewById(R.id.toolbar);
        t.setTitle("Mapa de circuitos");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.botones(getActivity());
        Toolbar t = getActivity().findViewById(R.id.toolbar);
        t.setTitle("Mapa de circuitos");
    }
}