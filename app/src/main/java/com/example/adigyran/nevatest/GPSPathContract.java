package com.example.adigyran.nevatest;

import android.provider.BaseColumns;

/**
 * Created by adigyran on 26.05.2016.
 */
public final class GPSPathContract {
    public GPSPathContract() {}
    public static abstract class GPSPathEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_DATETIME = "datetime";
        public static final String COLUMN_NAME_LAT = "latitude";
        public static final String COLUMN_NAME_LONG = "longitude";

    }
}

