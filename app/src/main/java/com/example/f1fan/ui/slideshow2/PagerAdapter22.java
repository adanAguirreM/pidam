package com.example.f1fan.ui.slideshow2;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.f1fan.ui.equipoView.EquipoFragment;
import com.example.f1fan.ui.rankingView.RakingFragment;
import com.example.f1fan.ui.recyclerView.pilotoFragment;
import com.example.f1fan.ui.temporadaView.TemporadaFragment;

public class PagerAdapter22 extends FragmentStateAdapter {
    public PagerAdapter22(FragmentActivity fm) {
        super(fm);
    }

    public Fragment createFragment(int position) {
        int a = 0;
        switch (position) {
            case 0: {
                Log.d("::TAG", "ranking");
                return new RakingFragment();
            }
            case 1: {
                return new TemporadaFragment();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
