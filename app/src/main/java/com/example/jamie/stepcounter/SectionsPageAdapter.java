package com.example.jamie.stepcounter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPageAdapter extends FragmentPagerAdapter {

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment1();
            case 1:
                return  new Fragment2();
            case 2:
                return  new Fragment3();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position){
            case 0:
                return "TAB 1";
            case 1:
                return  "Tabular 2";
            case 2:
                return  "3 TABS";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
