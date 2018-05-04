package com.example.jamie.stepcounter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class SectionsPageAdapter extends FragmentPagerAdapter {

    private final String[] tabHeadings = {"Daily", "Progress", "Diary"};

    private List<Fragment> fragments;
    private List<String> titles;


    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();

    }

    public void addFragment(Fragment frag, String title){
        fragments.add(frag);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return new Fragment1();
//            case 1:
//                return new Fragment2();
//            case 2:
//                return new Fragment3();
//            default:
//                return null;
//        }
        //
        if(position >= 0 && position <= fragments.size()){
            return fragments.get(position);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        if(position >= 0 && position <= titles.size()){
            return titles.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
