package com.example.jamie.stepcounter;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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
        sectionsPageAdapter.addFragment(new DailyFragment(), "Daily");
        sectionsPageAdapter.addFragment(new HistoryFragment(), "History");
        sectionsPageAdapter.addFragment(new Fragment3(), "Diary");

        //add adapter to pager
        viewPager.setAdapter(sectionsPageAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}