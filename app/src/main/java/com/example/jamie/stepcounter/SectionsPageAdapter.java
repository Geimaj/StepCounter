package com.example.jamie.stepcounter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPageAdapter extends FragmentPagerAdapter {

    private final String[] tabHeadings = {"Daily", "Progress", "Diary"};

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new Fragment3();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        if(position >= 0 && position <= tabHeadings.length){
            return tabHeadings[position];
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabHeadings.length;
    }
}
