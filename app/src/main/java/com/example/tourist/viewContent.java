package com.example.tourist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout;

public class viewContent extends AppCompatActivity {
    String tipsId, tableData, FileData;
    TextView content;
    SubtitleCollapsingToolbarLayout stoolbar;
    dbHelper dbHelper;
    ImageView imageView;
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

        dbHelper = new dbHelper(getApplicationContext());
        Cursor cursor = dbHelper.getAContent(tipsId, tableData);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String subtitle = tableData.equals("oyo_data") ? "Oyo State." : "Ogun State.";
            stoolbar.setSubtitle(subtitle);
            stoolbar.setTitle(cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDTITLE)));
            content.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENT))));
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
    }
}
