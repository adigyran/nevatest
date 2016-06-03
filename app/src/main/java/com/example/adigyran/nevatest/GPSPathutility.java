package com.example.adigyran.nevatest;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adigyran on 03.06.2016.
 */
public final class GPSPathutility {
    private GPSPathutility() {
    }
    public static String DateToString(Date imptd)
    {

        String returndate="";
        if(!(imptd==null))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {

                returndate = dateFormat.format(imptd);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return returndate;
    }
    public static Date StringToDate(String impts)
    {
        Date returnd = new Date();
        if(!(impts == null))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                returnd = format.parse(impts);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return returnd;
    }
}
