package com.example.tourist;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbHelper extends SQLiteOpenHelper {

    public static final String TAG = dbHelper.class.getSimpleName();
    public static int flag;
    // Exact Name of you db file that you put in assets folder with extension.
    static String DB_NAME = "touristData.db";
    private final Context myContext;
    String outFileName = "";
    private String DB_PATH;
    private SQLiteDatabase database;


    public dbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        ContextWrapper cw = new ContextWrapper(context);
        DB_PATH = cw.getFilesDir().getAbsolutePath() + "/databases/";
        Log.e(TAG, "dbHelper: DB_PATH " + DB_PATH);
        System.out.println("dbHelper: " + DB_PATH);
        outFileName = DB_PATH + DB_NAME;
        File file = new File(DB_PATH);
        Log.e(TAG, "dbHelper: " + file.exists());
        System.out.println("dbHelper: " + file.exists());
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            try {
                copyDataBase();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {

        Log.i("Database",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = myContext.getAssets().open(DB_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "New database has been copied to device!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createUserTable(){
        String CREATE_TABLE_USERSRECORD = "CREATE TABLE IF NOT EXISTS usersRecord ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullname VARCHAR, " +
                "email VARCHAR, " +
                "password VARCHAR )";
        database.execSQL(CREATE_TABLE_USERSRECORD);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

        }
    }


    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.e(TAG, "openDataBase: Open " + database.isOpen());
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }
    public Cursor getAllGroup(String tableName){
        String sql = "SELECT DISTINCT(contentgroup) FROM "+tableName +" ORDER BY contentgroup desc";
        return database.rawQuery(sql, null);
    }

    public Cursor getAllTableContent(String tableName){
        String sql = "SELECT * FROM "+tableName +" ORDER BY contentgroup desc";
        return database.rawQuery(sql, null);
    }

    public void UpdateFavourite(String recordId, String tableName){
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_FAVOURITE, 1);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public void DeleteFavourite(String recordId, String tableName){
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_FAVOURITE, 0);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public void UpdateVisited(String recordId, String tableName){
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_TRAVEL, 1);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public void DeleteVisited(String recordId, String tableName){
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_TRAVEL, 0);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public Cursor getAllFavourite(String tableName){
        String sql = "SELECT * FROM "+tableName+ " WHERE favourite = 1";
        return database.rawQuery(sql, null);
    }

    public Cursor getAllVisited(String tableName){
        String sql = "SELECT * FROM "+tableName+ " WHERE travel = 1";
        return database.rawQuery(sql, null);
    }

    public Cursor getAllGroupData(String group,String tableName){
        String sql = "SELECT * FROM "+tableName+ " WHERE contentgroup = '" + group +"'";
        return database.rawQuery(sql, null);
    }

    public Cursor getAPics(String recordId, String tableName){
        return database.query(tableName,
                null,
                dbColumnList.oyoData.COLUMN_RECORDID + " = ? Limit 1",
                new String[]{recordId},
                null,
                null,null);
    }

    public Cursor getAContent(String recordId, String tableName){
        return database.query(tableName,
                null,
                dbColumnList.oyoData.COLUMN_RECORDID +" = ? Limit 1",
                new String[]{recordId},
                null,
                null,null);
    }

    public void updateTable(String tableName, String recordId,String recordContent){
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_RECORDCONTENT, recordContent);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + " = ? ", new String[]{recordId});
    }


    public Cursor verifyUserExist(String email){
        String sql = "SELECT * FROM  usersRecord WHERE email = '" + email +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public boolean SaveUserInformation(String fullname, String email, String password){
        boolean res = false;
        ContentValues cv = new ContentValues();
        cv.put("fullname", fullname);
        cv.put("email", email);
        cv.put("password", password);
        Cursor cursor = verifyUserExist(email);
        if(cursor.getCount() < 1) {
            database.insert("usersRecord",null,cv);
            res=true;
        }
        return res;
    }
}
