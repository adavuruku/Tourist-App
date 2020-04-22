package com.example.tourist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new dbHelper(this);

        dbColumnList.oyoTab = new ArrayList<>();
        dbColumnList.ogunTab = new ArrayList<>();

        //check if the database file is existing
        File database = this.getDatabasePath(dbHelper.DATABASE_NAME);

        //if it has not copy it do this - copy it
        if(false == database.exists()){
            dbHelper.getReadableDatabase();
            if(copyDatabase(this)){
                Toast.makeText(this,"Copied",Toast.LENGTH_SHORT).show();
                //load all the groups for tab
                new LoadLocalData().execute();
            }else{
                Toast.makeText(this,"Not Copied",Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            //db existing load all the groups for tab
            new LoadLocalData().execute();
        }
    }

    private boolean copyDatabase(Context context){
        try {
            InputStream inputStream =context.getAssets().open(dbHelper.DATABASE_NAME);
            String outfilename = dbHelper.DBLOCATION + dbHelper.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outfilename);
            byte[] buff = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buff))> 0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    class LoadLocalData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Cursor cursor = dbHelper.getAllGroup(dbColumnList.oyoData.TABLE_NAME);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    dbColumnList.oyoTab.add(cursor.getString(cursor.getColumnIndex(dbColumnList.oyoData.COLUMN_RECORDCONTENTGROUP)));
                }
            }
            cursor.close();

            cursor = dbHelper.getAllGroup(dbColumnList.ogunData.TABLE_NAME);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    dbColumnList.ogunTab.add(cursor.getString(cursor.getColumnIndex(dbColumnList.ogunData.COLUMN_RECORDCONTENTGROUP)));
                }
            }
            cursor.close();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplication(),viewContent.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }
            },5000);


//            if(userID.equals("")){
//                Intent intent = new Intent(getApplication(),LoginScreen.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                finish();
//            }else{
//                Intent intent = new Intent(getApplication(), HomeScreen.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                finish();
//            }

        }
    }
}
