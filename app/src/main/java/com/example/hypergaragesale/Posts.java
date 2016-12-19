package com.example.hypergaragesale;

import android.provider.BaseColumns;

/*********************************************************************
 * CLASS NAME: Posts
 * PURPOSE:  This class each final variables mapping our database columns name
 *
 **********************************************************************/

public class Posts {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Posts() {}

    /* Inner class that defines the table contents */
    public static abstract class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PICTURE_CONTENT = "picture";
        public static final String COLUMN_NAME_MOOD_RATE = "mood";
        public static final String COLUMN_NAME_LOCATION = "location";

    }
}
