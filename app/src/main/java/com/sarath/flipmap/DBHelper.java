package com.sarath.flipmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by vinoth on 9/1/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String DBNAME="contact.db";
    private static  int DB_VERSION=1;


    public DBHelper(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " ( " +
                ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                ContactContract.ContactEntry.Column_NAME + " TEXT NOT NULL , " +
                ContactContract.ContactEntry.COLUMN_EMAIL + " TEXT NOT NULL , " +
                ContactContract.ContactEntry.COLUMN_HOMENO + " TEXT NOT NULL , " +
                ContactContract.ContactEntry.COLUMN_PHONENO + " TEXT NOT NULL " + ")";

        db.execSQL(CREATE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE "+ ContactContract.ContactEntry.TABLE_NAME);

    }
}
