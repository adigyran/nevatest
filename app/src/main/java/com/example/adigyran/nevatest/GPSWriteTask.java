package com.example.adigyran.nevatest;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

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
        Log.d("nevatest", "doInBackground: "+String.valueOf(gpswritelist.getGPSPoints().size()));
        long newRowId = 0;
        gpswdb.beginTransaction();
        try {

            for (GPSPathpoint wrtpoint : gpswritelist.getGPSPoints()) {
                Log.d("nevatest", "doInBackground: " + String.valueOf(wrtpoint.getPointdatetime()+" "+GPSPathutility.DateToString(wrtpoint.getPointdatetime())));
                //values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_DATETIME, GPSPathutility.DateToString(wrtpoint.getPointdatetime()));
                values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_DATETIME,GPSPathutility.DateToLong(wrtpoint.getPointdatetime()));
                values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_LAT, wrtpoint.getPLatitude());
                values.put(GPSPathContract.GPSPathEntry.COLUMN_NAME_LONG, wrtpoint.getPLongitude());
                newRowId = gpswdb.insert(TABLE_NAME, null, values);
            }
            gpswdb.setTransactionSuccessful();
        }
        catch (SQLException e)
        {

        }
        finally {
            gpswdb.endTransaction();
        }

        Log.d("nevatest", "doInBackground: "+String.valueOf(newRowId));
        gpswdb.close();
        return null;
    }



    @Override
    protected void onPostExecute(Integer integer) {
        gpswdb.close();
        super.onPostExecute(integer);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }


}