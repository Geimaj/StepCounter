package com.example.jamie.stepcounter;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements Fragment3.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPageAdapter sectionsPageAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init tab layout
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        //init viewPager
        viewPager = (ViewPager) findViewById(R.id.main_pager);

        //create page adapter
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        //add fragments to adapter
        sectionsPageAdapter.addFragment(new Fragment1(), "Frag 1");
        sectionsPageAdapter.addFragment(new Fragment2(), "Frag TWO");
        sectionsPageAdapter.addFragment(new Fragment3(), "Fragment 3");

        //add adapter to pager
        viewPager.setAdapter(sectionsPageAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}