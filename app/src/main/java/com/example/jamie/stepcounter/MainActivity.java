package com.example.jamie.stepcounter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final String[] tabHeadings = {"Daily", "Progress", "Diary"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add toolbar to activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init tab layout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //add tabs
        for (String heading : tabHeadings) {
            tabLayout.addTab(tabLayout.newTab().setText(heading));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //init viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //create page adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //add adapter to pager
        viewPager.setAdapter(adapter);

        //add onSelectedTabListener to swipe views
        tabLayout.setOnTabSelectedListener(this);


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabLayout.setScrollPosition(tab.getPosition(), 0f,true);
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}