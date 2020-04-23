package com.example.tourist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;

public class viewContent extends AppCompatActivity {
    String tipsId, tableData, FileData, title, subtitle, body,visitStatus, favouriteStatus;
    TextView content;
    SubtitleCollapsingToolbarLayout stoolbar;
    dbHelper dbHelper;
    ImageView imageView;
    ImageButton visited, favourite;
    Button btnPhone, btnVisit, btnShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        tipsId = getIntent().getStringExtra("tipsId");
        tableData = getIntent().getStringExtra("tableData");
        FileData = getIntent().getStringExtra("FileData");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        content = findViewById(R.id.body);
        stoolbar = findViewById(R.id.collapsing);
        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Hello Clicked");
            }
        });
        btnPhone = findViewById(R.id.btnPhone);
        btnVisit = findViewById(R.id.btnVisit);
        btnShare = findViewById(R.id.btnShare);

        visited = findViewById(R.id.visited);
        favourite = findViewById(R.id.favourite);

        dbHelper = new dbHelper(getApplicationContext());
        Cursor cursor = dbHelper.getAContent(tipsId, tableData);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            subtitle = tableData.equals("oyo_data") ? "Oyo State." : "Ogun State.";
            title = cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDTITLE));
            body = cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENT));
            stoolbar.setSubtitle(subtitle);
            stoolbar.setTitle(title);
            content.setText(body);
            visitStatus = cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_TRAVEL));
            favouriteStatus = cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_FAVOURITE));
            if (visitStatus.equals("1")) {
                visited.setBackgroundResource(R.drawable.circleselected);
            }

            if (favouriteStatus.equals("1")) {
                favourite.setBackgroundResource(R.drawable.circleselected);
            }

        }
        cursor.close();

        Cursor picsCursor = dbHelper.getAPics(tipsId,FileData);
        if(picsCursor.getCount()>0){
            picsCursor.moveToFirst();
            byte[] content = picsCursor.getBlob(picsCursor.getColumnIndexOrThrow(dbColumnList.oyoFile.COLUMN_FILEDATA));
            Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);
            imageView.setImageBitmap(bitmap);
        }
        picsCursor.close();


        //call

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_id = tableData.equals("oyo_data") ? "tel:08037283852" : "tel:08103341344";
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phone_id));
                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    if (ActivityCompat.checkSelfPermission(viewContent.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,title + System.getProperty("line.separator") + subtitle);
                sharingIntent.putExtra(Intent.EXTRA_TEXT,body + System.getProperty("line.separator") +
                        System.getProperty("line.separator") +
                        "Courtesy - Tourist App (Oyo & Ogun) State. By: Adeyemi Adebunmi Teslimat");
                startActivity(Intent.createChooser(sharingIntent,"Share Via"));
            }
        });





        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visitStatus.equals("1")){
                    dbHelper.DeleteVisited(tipsId,tableData);
                    visitStatus = "0";
                    visited.setBackgroundResource(R.drawable.circle);
                    Toast.makeText(getApplicationContext(),  "Remove " + title +" To Visited Places.",Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.UpdateVisited(tipsId,tableData);
                    visited.setBackgroundResource(R.drawable.circleselected);
                    visitStatus = "1";
                    Toast.makeText(getApplicationContext(),  "Add " + title +" To Visited Places." ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favouriteStatus.equals("1")){
                    dbHelper.DeleteFavourite(tipsId,tableData);
                    favouriteStatus="0";
                    favourite.setBackgroundResource(R.drawable.circle);
                    Toast.makeText(getApplicationContext(),  "Remove " +title + " To Favourite." ,Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.UpdateFavourite(tipsId,tableData);
                    favouriteStatus="1";
                    favourite.setBackgroundResource(R.drawable.circleselected);
                    Toast.makeText(getApplicationContext(),  "Add " +title + " To Favourite." ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
