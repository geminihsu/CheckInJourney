package com.example.hypergaragesale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/********************************************************************************
 * CLASS NAME: PostsDbHelper
 * PURPOSE: This class take over the all operators from SQLite
 * MEMBER FUNCTIONS: *
 * void onCreate(SQLiteDatabase db);
 * void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
 * onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
 * void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
 * *******************************************************************************/
public class PostsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Posts.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Posts.PostEntry.TABLE_NAME + " (" +
                    Posts.PostEntry._ID + " INTEGER PRIMARY KEY," +
                    Posts.PostEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_PICTURE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_PRICE + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_MOOD_RATE + TEXT_TYPE + COMMA_SEP +
                    Posts.PostEntry.COLUMN_NAME_LOCATION + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Posts.PostEntry.TABLE_NAME;

    public PostsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*********************************************************************
     * FUNCTION: onCreate
     * PURPOSE: create database schema
     *
     * PARAMETERS: SQLiteDatabase db
     **********************************************************************/
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /*********************************************************************
     * FUNCTION: onUpgrade
     * PURPOSE: replace old database with new database
     *
     * PARAMETERS: SQLiteDatabase db, int oldVersion, int newVersion
     **********************************************************************/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    /*********************************************************************
     * FUNCTION: onUpgrade
     * PURPOSE: replace new database with old database
     *
     * PARAMETERS: SQLiteDatabase db, int oldVersion, int newVersion
     **********************************************************************/
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
