package com.example.f1fan.ui.slideshow;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.f1fan.ui.equipoView.EquipoFragment;
import com.example.f1fan.ui.recyclerView.pilotoFragment;

public class PagerAdapter2 extends FragmentStateAdapter {
    public PagerAdapter2(FragmentActivity fm) {
        super(fm);
    }

    public Fragment createFragment(int position) {
        int a = 0;
        switch (position) {
            case 0: {
                return new EquipoFragment();
            }
            case 1: {
                return new pilotoFragment();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
