package com.example.adigyran.nevatest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import static com.example.adigyran.nevatest.GPSPathContract.GPSPathEntry.TABLE_NAME;

/**
 * Created by adigyran on 26.05.2016.
 */
public  class GPSReadTask extends AsyncTask<GPSPathDbghelper,Integer,GPSPathlist>{

    private GPSPathDbghelper asyngpsread =null;
    private SQLiteDatabase gpsrdb = null;
    private GPSPathlist gpsreadlist = null;

    public GPSReadTask(GPSPathDbghelper asyngpswrite, GPSPathlist inptlist) {
       // asyngpswrite = null;
        this.asyngpsread = asyngpswrite;
        this.gpsreadlist = inptlist;

    }

    public GPSPathlist getGpsreadlist() {
        return gpsreadlist;
    }

    @Override
    protected GPSPathlist doInBackground(GPSPathDbghelper... params) {
        gpsrdb =  asyngpsread.getReadableDatabase();
        String[] projection = {
                GPSPathContract.GPSPathEntry._ID,
                GPSPathContract.GPSPathEntry.COLUMN_NAME_DATETIME,
                GPSPathContract.GPSPathEntry.COLUMN_NAME_LAT,
                GPSPathContract.GPSPathEntry.COLUMN_NAME_LONG};
        String sortOrder =
                GPSPathContract.GPSPathEntry.COLUMN_NAME_DATETIME + " DESC";
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = gpsrdb.query(
                GPSPathContract.GPSPathEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return null;
    }





    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


}