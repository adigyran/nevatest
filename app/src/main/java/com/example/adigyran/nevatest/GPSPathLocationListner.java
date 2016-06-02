package com.example.adigyran.nevatest;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by adigyran on 02.06.2016.
 */
public class GPSPathLocationListner implements LocationListener {

    private GPSPathlist loclistpl = null;
    private boolean recording = false;
    public GPSPathLocationListner() {

    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public GPSPathlist getLoclistpl() {
        return loclistpl;
    }

    public void setLoclistpl(GPSPathlist loclistpl) {
        this.loclistpl = loclistpl;
    }

    @Override
    public void onLocationChanged(Location location) {
        if((!(loclistpl==null))&&recording)
        {
            if(!(loclistpl.getGPSPoints()==null))
            {
                GPSPathpoint testpoint = new GPSPathpoint();
                testpoint.setPointdatetime(new Date());
                testpoint.setPLatitude(location.getLatitude());
                testpoint.setPLongitude(location.getLongitude());
                loclistpl.addGPSPoint(testpoint);

            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
