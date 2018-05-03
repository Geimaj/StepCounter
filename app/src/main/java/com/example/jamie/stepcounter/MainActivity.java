package com.example.jamie.stepcounter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPageAdapter adapter;

    private final String[] tabHeadings = {"Daily", "Progress", "Diary"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new SectionsPageAdapter(getSupportFragmentManager());

        //set up view pager with sections adapter
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        adapter.addFragment(new Tab1(), tabHeadings[0]);
        adapter.addFragment(new Tab2(), tabHeadings[1]);
        adapter.addFragment(new Tab3(), tabHeadings[2]);
        viewPager.setAdapter(adapter);
    }

}