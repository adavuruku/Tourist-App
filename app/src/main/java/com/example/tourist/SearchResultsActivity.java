package com.example.tourist;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener {
    String tableName;
    List<myModels.contentModel> contentData, newList;
    RecyclerView recyclerView;
    private contentAdapter contentAdapter;
    dbHelper dbHelper;
    SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        tableName = getIntent().getStringExtra("tableName");

        contentData = new ArrayList<>();

        String state = tableName.equals("oyo_data")? "Search Oyo State.":"Search Ogun State.";
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(state);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new loadData().execute();
            }
        },200);

        search = findViewById(R.id.searchData);
        search.setOnQueryTextListener(this);

    }



    class loadData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            dbHelper = new dbHelper(getApplicationContext());
            dbHelper.openDataBase();
            Cursor cursor = dbHelper.getAllTableContent(tableName);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    Cursor picsCursor = dbHelper.getAPics(cursor.getString(cursor.getColumnIndex(dbColumnList.ogunData.COLUMN_RECORDID)),
                            dbColumnList.ogunFile.TABLE_NAME);
                    if(picsCursor.getCount()>0){
                        picsCursor.moveToFirst();

                        myModels.contentModel contentList = new myModels().new contentModel(
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDTITLE)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDID)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENT)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENTGROUP)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_TRAVEL)),
                                cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_FAVOURITE)),
                                picsCursor.getBlob(picsCursor.getColumnIndexOrThrow(dbColumnList.ogunFile.COLUMN_FILEDATA))
                        );
                        contentData.add(contentList);
                    }
                    picsCursor.close();

                }
            }
            cursor.close();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }



    @Override
    public boolean onQueryTextChange(String textData){
        newList = new ArrayList<>();
        String newText = textData.toLowerCase();
        for(myModels.contentModel cont : contentData){
            String name_ = cont.getTitle().toLowerCase();
            String depart_ = cont.getContent().toLowerCase();
            String group = cont.getContentGroup().toLowerCase();
            if(name_.contains(newText) || depart_.contains(newText)|| group.contains(newText)){
                newList.add(cont);
            }
        }
        contentAdapter = new contentAdapter(newList, getApplicationContext(), new contentAdapter.OnItemClickListener() {
            @Override
            public void onVisitedClick(View v, int position) {
                ImageButton visited = (ImageButton)v;
                String postid =  contentData.get(position).getRecordId();
                String placevisit =  contentData.get(position).getTitle();
                if(contentData.get(position).getTravel().equals("1")){
                    visited.setBackgroundResource(R.drawable.circle);
                    dbHelper.DeleteVisited(postid,dbColumnList.ogunData.TABLE_NAME);
                    contentData.get(position).setTravel("0");
                    Toast.makeText(getApplicationContext(),  "Removed " +placevisit +" From Visited" ,Toast.LENGTH_SHORT).show();
                }else{
                    visited.setBackgroundResource(R.drawable.circleselected);
                    dbHelper.UpdateVisited(postid,dbColumnList.ogunData.TABLE_NAME);
                    contentData.get(position).setTravel("1");
                    Toast.makeText(getApplicationContext(),  "Added " +placevisit +" To Visited" ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFavouriteClick(View v, int position) {
                ImageButton favourite = (ImageButton)v;
                String postid =  contentData.get(position).getRecordId();
                String placevisit =  contentData.get(position).getTitle();
                if(contentData.get(position).getFavourite().equals("1")){
                    favourite.setBackgroundResource(R.drawable.circle);
                    dbHelper.DeleteFavourite(postid,dbColumnList.ogunData.TABLE_NAME);
                    contentData.get(position).setFavourite("0");
                    Toast.makeText(getApplicationContext(),  "Removed " +placevisit +" From Favourite" ,Toast.LENGTH_SHORT).show();
                }else{
                    favourite.setBackgroundResource(R.drawable.circleselected);
                    dbHelper.UpdateFavourite(postid,dbColumnList.ogunData.TABLE_NAME);
                    contentData.get(position).setFavourite("1");
                    Toast.makeText(getApplicationContext(),  "Added " +placevisit +" To Favourite" ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onImageClick(View v, int position) {
                String postid =  contentData.get(position).getRecordId();
//                    Toast.makeText(getContext(), postid + " Favourite",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), viewContent.class);
                intent.putExtra("tipsId",postid);
                intent.putExtra("tableData",dbColumnList.ogunData.TABLE_NAME);
                intent.putExtra("FileData",dbColumnList.ogunFile.TABLE_NAME);
                intent.putExtra("acivity","search");
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        contentAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(contentAdapter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(tableName.equals("oyo_data")){
                    Intent intent = new Intent(this, OyoHome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, OgunHome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
