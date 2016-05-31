package com.example.adigyran.nevatest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.adigyran.nevatest.GPSPathContract;

/**
 * Created by adigyran on 26.05.2016.
 */
public class GPSPathDbghelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATETIME_TYPE = " TIMESTAMP";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GPSPathContract.GPSPathEntry.TABLE_NAME + " (" +
                    GPSPathContract.GPSPathEntry._ID + " INTEGER PRIMARY KEY," +
                    GPSPathContract.GPSPathEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    GPSPathContract.GPSPathEntry.COLUMN_NAME_DATETIME + DATETIME_TYPE + COMMA_SEP +
                    GPSPathContract.GPSPathEntry.COLUMN_NAME_LAT + DOUBLE_TYPE + COMMA_SEP +
                    GPSPathContract.GPSPathEntry.COLUMN_NAME_LONG + DOUBLE_TYPE + COMMA_SEP+")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GPSPathContract.GPSPathEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GPSPath.db";


    public GPSPathDbghelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
       // db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //   onUpgrade(db, oldVersion, newVersion);
    }
}
