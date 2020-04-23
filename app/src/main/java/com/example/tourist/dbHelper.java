package com.example.tourist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbHelper extends SQLiteOpenHelper {
    // Database Info
    // Database Info
    public static final String DATABASE_NAME = "touristData.db";
    public static final String DBLOCATION = "/data/data/com.example.tourist/databases/";
    private static final int DATABASE_VERSION = 1;
    private Context mcontext;
    private SQLiteDatabase mdatabase;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mcontext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

        }
    }
    public Cursor getAllGroup(String tableName){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT DISTINCT(contentgroup) FROM "+tableName +" ORDER BY contentgroup desc";
        return database.rawQuery(sql, null);
    }


    public void UpdateFavourite(String recordId, String tableName){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_FAVOURITE, 1);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public void DeleteFavourite(String recordId, String tableName){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_FAVOURITE, 0);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public void UpdateVisited(String recordId, String tableName){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_TRAVEL, 1);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public void DeleteVisited(String recordId, String tableName){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_TRAVEL, 0);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + "= ?", new String[]{recordId});
    }

    public Cursor getAllFavourite(String tableName){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+tableName+ " WHERE favourite = 1";
        return database.rawQuery(sql, null);
    }

    public Cursor getAllVisited(String tableName){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+tableName+ " WHERE travel = 1";
        return database.rawQuery(sql, null);
    }

    public Cursor getAllGroupData(String group,String tableName){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+tableName+ " WHERE contentgroup = '" + group +"'";
        return database.rawQuery(sql, null);
    }

    public Cursor getAPics(String recordId, String tableName){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(tableName,
                null,
                dbColumnList.oyoData.COLUMN_RECORDID + " = ? Limit 1",
                new String[]{recordId},
                null,
                null,null);
    }

    public Cursor getAContent(String recordId, String tableName){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(tableName,
                null,
                dbColumnList.oyoData.COLUMN_RECORDID +" = ? Limit 1",
                new String[]{recordId},
                null,
                null,null);
    }

    public void updateTable(String tableName, String recordId,String recordContent){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.oyoData.COLUMN_RECORDCONTENT, recordContent);
        database.update(tableName, cv, dbColumnList.oyoData.COLUMN_RECORDID + " = ? ", new String[]{recordId});
    }
}
