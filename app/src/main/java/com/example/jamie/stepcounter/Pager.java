package com.example.jamie.stepcounter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Pager extends FragmentStatePagerAdapter{

    private int numTabs;

    public Pager(FragmentManager fm, int tabCount){
        super(fm);
        this.numTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
            case 2:
                return new Tab3();
        }

        return null;
    }

    @Override
    public int getCount() {
        return this.numTabs;
    }
}
