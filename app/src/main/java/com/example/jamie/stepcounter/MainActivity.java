package com.example.jamie.stepcounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPageAdapter sectionsPageAdapter;
    private LoginActivity loginActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //display login
        loginActivity = new LoginActivity();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init tab layout
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        //init viewPager
        viewPager = (ViewPager) findViewById(R.id.main_pager);

        //create page adapter
        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        //add fragments to adapter
        sectionsPageAdapter.addFragment(new DailyFragment(), "Daily");
        sectionsPageAdapter.addFragment(new HistoryFragment(), "History");
        sectionsPageAdapter.addFragment(new DiaryFragment(), "Diary");

        //add adapter to pager
        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);


        return super.onOptionsItemSelected(item);
    }


}