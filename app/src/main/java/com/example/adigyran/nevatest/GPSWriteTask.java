package com.example.adigyran.nevatest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import static com.example.adigyran.nevatest.GPSPathContract.GPSPathEntry.TABLE_NAME;

/**
 * Created by adigyran on 26.05.2016.
 */
public  class GPSWriteTask extends AsyncTask<GPSPathDbghelper,Integer,Integer>{

    private GPSPathDbghelper asyngpswrite =null;
    private SQLiteDatabase gpswdb = null;
    private GPSPathlist gpswritelist = null;

    public GPSWriteTask(GPSPathDbghelper asyngpswrite,GPSPathlist inptlist) {
       // asyngpswrite = null;
        this.asyngpswrite = asyngpswrite;
        this.gpswritelist = inptlist;
    }

    @Override
    protected Integer doInBackground(GPSPathDbghelper... params) {
        gpswdb =  asyngpswrite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_DATETIME, String.valueOf(gpswritelist.getGPSPoints().get(0).getPointdatetime()));
        values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_LAT,gpswritelist.getGPSPoints().get(0).getPLatitude());
        values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_LONG,gpswritelist.getGPSPoints().get(0).getPLongitude());
        long newRowId;
        newRowId = gpswdb.insert(TABLE_NAME,null,values);
        return null;
    }



    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


}