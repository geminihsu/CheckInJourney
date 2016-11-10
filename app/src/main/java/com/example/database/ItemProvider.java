package com.example.database;

/**
 * Created by geminihsu on 2016/11/9.
 */
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;

import android.database.Cursor;
import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import android.net.Uri;
import android.text.TextUtils;

public class ItemProvider extends ContentProvider{
        static final String PROVIDER_NAME = "com.example.hypergaragesale.ItemsProvider";
        static final String URL = "content://" + PROVIDER_NAME + "/items";
        public static final Uri CONTENT_URI = Uri.parse(URL);

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE_CONTENT = "image_content";

        private static HashMap<String, String> ITEM_PROJECTION_MAP;

        static final int ITEM = 1;
        static final int ITEM_ID = 2;

        static final UriMatcher uriMatcher;
        static{
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            uriMatcher.addURI(PROVIDER_NAME, "items", ITEM);
            uriMatcher.addURI(PROVIDER_NAME, "items/#", ITEM_ID);
        }

        /**
         * Database specific constant declarations
         */

        private SQLiteDatabase db;
        static final String DATABASE_NAME = "GarageSale";
        static final String ITEMS_TABLE_NAME = "items";
        static final int DATABASE_VERSION = 1;
        static final String CREATE_DB_TABLE =
                " CREATE TABLE " + ITEMS_TABLE_NAME +
                        " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " title TEXT NOT NULL, " +
                        " price TEXT NOT NULL, " +
                        " description TEXT NOT NULL, " +
                        " image_content TEXT NOT NULL);";

        /**
         * Helper class that actually creates and manages
         * the provider's underlying data repository.
         */

        private static class DatabaseHelper extends SQLiteOpenHelper {
            DatabaseHelper(Context context){
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_DB_TABLE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " +  ITEMS_TABLE_NAME);
                onCreate(db);
            }
        }

        @Override
        public boolean onCreate() {
            Context context = getContext();
            DatabaseHelper dbHelper = new DatabaseHelper(context);

            /**
             * Create a write able database which will trigger its
             * creation if it doesn't already exist.
             */

            db = dbHelper.getWritableDatabase();
            return (db == null)? false:true;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
            /**
             * Add a new student record
             */
            long rowID = db.insert(	ITEMS_TABLE_NAME, "", values);

            /**
             * If record is added successfully
             */
            if (rowID > 0) {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            }

            throw new SQLException("Failed to add a record into " + uri);
        }

        @Override
        public Cursor query(Uri uri, String[] projection,
                            String selection,String[] selectionArgs, String sortOrder) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(ITEMS_TABLE_NAME);

            switch (uriMatcher.match(uri)) {
                case ITEM:
                    qb.setProjectionMap(ITEM_PROJECTION_MAP);
                    break;

                case ITEM_ID:
                    qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
            }

            if (sortOrder == null || sortOrder == ""){
                /**
                 * By default sort on student names
                 */
                sortOrder = TITLE;
            }

            Cursor c = qb.query(db,	projection,	selection,
                    selectionArgs,null, null, sortOrder);
            /**
             * register to watch a content URI for changes
             */
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)){
                case ITEM:
                    count = db.delete(ITEMS_TABLE_NAME, selection, selectionArgs);
                    break;

                case ITEM_ID:
                    String id = uri.getPathSegments().get(1);
                    count = db.delete( ITEMS_TABLE_NAME, _ID +  " = " + id +
                                    (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }

        @Override
        public int update(Uri uri, ContentValues values,
                          String selection, String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case ITEM:
                    count = db.update(ITEMS_TABLE_NAME, values, selection, selectionArgs);
                    break;

                case ITEM_ID:
                    count = db.update(ITEMS_TABLE_NAME, values,
                            _ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ? "AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }

        @Override
        public String getType(Uri uri) {
            switch (uriMatcher.match(uri)){
                /**
                 * Get all student records
                 */
                case ITEM:
                    return "vnd.android.cursor.dir/vnd.example.students";
                /**
                 * Get a particular student
                 */
                case ITEM_ID:
                    return "vnd.android.cursor.item/vnd.example.students";
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        }
}
