package com.example.tourist;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Random;

public class OyoHome extends AppCompatActivity implements OyoScreen.OnFragmentInteractionListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    dbHelper dbHelper;
    Toolbar toolbar;
    private SharedPreferences LoginUserEmail;
    String UserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyo_home);

        LoginUserEmail = this.getSharedPreferences("LoginUserEmail", this.MODE_PRIVATE);
        UserEmail= LoginUserEmail.getString("LoginUserEmail", "");

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
                    case R.id.visited:
                        intent = new Intent(getApplicationContext(), OyoFavourite.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.ogun:
                        intent = new Intent(getApplicationContext(), OgunHome.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.about:
                        intent = new Intent(getApplicationContext(), about.class);
                        intent.putExtra("tableName", "oyo_data");
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case R.id.profile:
                        showProfile();
                        break;
                    case R.id.close:
                        getApplicationContext().getSharedPreferences("LoginUserEmail",0).edit().clear().apply();

                        intent = new Intent(getApplicationContext(), LoginOption.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        break;
                }
                return true;
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        final CoordinatorLayout content =  findViewById(R.id.content);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            private float scaleFactor = 6f;
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
                content.setScaleX(1 - (slideOffset / scaleFactor));
                content.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerElevation(0f);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.searchmenu, menu);
//        MenuItem searchItem = menu.findItem(R.id.search);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//            searchView.setIconifiedByDefault(false);
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//
//    @Override
//    public boolean onSearchRequested() {
//        Bundle appData = new Bundle();
//        appData.putString("tableName", "oyo_data");
//        startSearch(null, false, appData, false);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.search:
                intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                intent.putExtra("tableName", "oyo_data");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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


    public void showProfile(){
        View snackView = getLayoutInflater().inflate(R.layout.customprofile, null);

        TextView user = snackView.findViewById(R.id.user);
        TextView email = snackView.findViewById(R.id.email);
        dbHelper = new dbHelper(getApplicationContext());
        dbHelper.openDataBase();
        Cursor cur =dbHelper.getAUser(UserEmail);
        if(cur.getCount()>0){
            cur.moveToFirst();
            user.setText(
                    cur.getString(cur.getColumnIndex("fullname"))
            );
            email.setText(
                    cur.getString(cur.getColumnIndex("email"))
            );
        }
        cur.close();

        final Dialog d = new Dialog(OyoHome.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCanceledOnTouchOutside(true);
        d.setContentView(snackView);
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        d.show();
    }
}
