package com.example.tourist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    dbHelper dbHelper;
    private SharedPreferences LoginUserEmail;
    String UserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginUserEmail = this.getSharedPreferences("LoginUserEmail", this.MODE_PRIVATE);
        UserEmail= LoginUserEmail.getString("LoginUserEmail", "");

        dbHelper = new dbHelper(this);

        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
            new LoadLocalData().execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        dbColumnList.oyoTab = new ArrayList<>();
        dbColumnList.ogunTab = new ArrayList<>();

//        //check if the database file is existing
//        File database = this.getDatabasePath(dbHelper.DATABASE_NAME);
//
//        //if it has not copy it do this - copy it
//        if(false == database.exists()){
////            dbHelper.getReadableDatabase();
//            if(copyDatabase(this)){
//                Toast.makeText(this,"Copied",Toast.LENGTH_SHORT).show();
//                //load all the groups for tab
//                new LoadLocalData().execute();
//            }else{
//                Toast.makeText(this,"Not Copied",Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }else{
//            //db existing load all the groups for tab
//            new LoadLocalData().execute();
//        }


    }

//    private boolean copyDatabase(Context context){
//        try {
//            InputStream inputStream =context.getAssets().open(dbHelper.DATABASE_NAME);
//            String outfilename = dbHelper.DBLOCATION + dbHelper.DATABASE_NAME;
//            OutputStream outputStream = new FileOutputStream(outfilename);
//            byte[] buff = new byte[1024];
//            int length = 0;
//            while((length = inputStream.read(buff))> 0){
//                outputStream.write(buff,0,length);
//            }
//            outputStream.flush();
//            outputStream.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

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


//            String json;
//            InputStream is;
//            try {
//                is = getApplication().getAssets().open("oyo_data.json");
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//
//                json = new String(buffer, "UTF-8");
//                JSONArray jsonarray = new JSONArray(json);
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    JSONObject jsonobject = jsonarray.getJSONObject(i);
//
//                    dbHelper.updateTable(dbColumnList.oyoData.TABLE_NAME,
//                            jsonobject.getString("id"),
//                            jsonobject.getString("content"));
//
//                }
//
//
//                is = getApplication().getAssets().open("ogun_data.json");
//                size = is.available();
//                buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//
//                json = new String(buffer, "UTF-8");
//                jsonarray = new JSONArray(json);
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    JSONObject jsonobject = jsonarray.getJSONObject(i);
//
//                    dbHelper.updateTable(dbColumnList.ogunData.TABLE_NAME,
//                            jsonobject.getString("id"),
//                            jsonobject.getString("content"));
//
//                }
//
//
////
////
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(UserEmail.equals("")){
                        Intent intent = new Intent(getApplication(),LoginOption.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }else{
                        Intent intent = new Intent(getApplication(),OgunHome.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                    }

                }
            },3000);



        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
