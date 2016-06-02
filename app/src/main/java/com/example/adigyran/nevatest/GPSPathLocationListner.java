package com.example.adigyran.nevatest;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by adigyran on 02.06.2016.
 */
public class GPSPathLocationListner implements LocationListener {

    private GPSPathlist loclistpl = null;
    public GPSPathLocationListner() {

    }

    public GPSPathlist getLoclistpl() {
        return loclistpl;
    }

    public void setLoclistpl(GPSPathlist loclistpl) {
        this.loclistpl = loclistpl;
    }

    @Override
    public void onLocationChanged(Location location) {

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
