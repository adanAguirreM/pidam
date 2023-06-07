package com.example.f1fan.ui.slideshow2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.f1fan.R;
import com.example.f1fan.databinding.FragmentSlideshowBinding;
import com.example.f1fan.modelo.pojos.Rol;
import com.example.f1fan.modelo.pojos.Usuario;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SlideshowFragment2 extends Fragment {

    private FragmentSlideshowBinding binding;

    private TabLayout tabs;
    private ViewPager2 viewPager2;
    private PagerAdapter22 pagerAdapter2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel2 slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel2.class);
//        getActivity().getActionBar().setTitle("Equipos y pilotos");
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // PARA EL ARRASTRE DE DEDO
        viewPager2 = binding.viewPager2;
        pagerAdapter2 = new PagerAdapter22(this.getActivity());
        viewPager2.setAdapter(pagerAdapter2);

        // PARA LAS PESTAÑAS
        tabs = binding.tabs;
        new TabLayoutMediator(tabs,viewPager2,(tab,position)->{
            if (position==0)tab.setText("Ranking");
            if (position==1)tab.setText("Histórico");
        }).attach();
        //final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Toolbar t = (Toolbar) getActivity().findViewById(R.id.toolbar);
        t.setTitle("Ranking e histórico");

        if (Usuario.getRol() == Rol.ANONIMO) {
            getActivity().findViewById(R.id.slideshowFragment2).setEnabled(false);
            getActivity().findViewById(R.id.slideshowFragment).setEnabled(false);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar t = (Toolbar) getActivity().findViewById(R.id.toolbar);
        t.setTitle("Ranking e histórico");
    }
}