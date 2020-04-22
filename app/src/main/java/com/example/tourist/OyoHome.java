package com.example.tourist;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Random;

public class OyoHome extends AppCompatActivity implements OyoScreen.OnFragmentInteractionListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    dbHelper dbHelper;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyo_home);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        drawerLayout = findViewById(R.id.drawer_layout);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setSaveEnabled(true);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        //retrieve student information
        dbHelper = new dbHelper(getApplicationContext());

        initNavigationDrawer();
    }

    public void initNavigationDrawer() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                drawerLayout.closeDrawers();
                switch (id) {
                    case R.id.favourite:
//                        intent = new Intent(getApplicationContext(), DailyTips.class);
//                        intent.putExtra("TABID","0");
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.visited:
//                        intent = new Intent(getApplicationContext(), MonthlyTips.class);
//                        intent.putExtra("TABID","0");
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.ogun:
                        intent = new Intent(getApplicationContext(), OgunHome.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        break;
                    case R.id.about:
//                        intent = new Intent(getApplicationContext(), about.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.close:
                        finish();
                        break;
                }
                return true;
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) OyoHome.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(OyoHome.this.getComponentName()));
        }
        System.out.println("You Clicked");
        return super.onCreateOptionsMenu(menu);
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.searchmenu, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return OyoScreen.getInstance(position);
        }



        @Override
        public int getCount() {
            return dbColumnList.oyoTab.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dbColumnList.oyoTab.get(position).toUpperCase();
        }
    }
}
