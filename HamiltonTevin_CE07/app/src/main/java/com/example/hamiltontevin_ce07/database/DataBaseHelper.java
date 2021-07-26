// Tevin Hamilton
// JAV2 - 2003
// File - DataBaseHelper
package com.example.hamiltontevin_ce07.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_FILE = "articleDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "articles";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_END_URL = "endUrl";
    public static final String COLUMN_IMAGE_LINK = "imageLink";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + "  TEXT, " +
            COLUMN_END_URL + " TEXT, " +
            COLUMN_IMAGE_LINK + " INTEGER)";


    private static SQLiteDatabase mDatabase;
    private static DataBaseHelper mInstance = null;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private DataBaseHelper(Context _context) {
        super(_context, DATABASE_FILE, null, DATABASE_VERSION);
        mDatabase = getWritableDatabase();
    }
    
    public static DataBaseHelper getInstance(Context _context) {
        if(mInstance == null) {
            mInstance = new DataBaseHelper(_context);
        }
        return mInstance;
    }


    public static void insertArticles(String[] postStrings){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,postStrings[0]);
        cv.put(COLUMN_END_URL,postStrings[1]);
        cv.put(COLUMN_IMAGE_LINK,postStrings[2]);

        mDatabase.insert(TABLE_NAME, null, cv);
    }




    public static Cursor getAllArticles() {
        return mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
    }
}
