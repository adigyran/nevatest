package com.example.adigyran.nevatest;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final IBinder TRKBinder = new LocalBinder();

    private GoogleApiClient mGoogleApiClient = null;
    MapsActivity actmaps;


    public TrackingService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return TRKBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setActmaps(MapsActivity actmaps) {
        this.actmaps = actmaps;
    }

    public class LocalBinder extends Binder {
        TrackingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackingService.this;
        }
    }

    public void Record(boolean isrecord, GoogleMap mMap) {
        double cur_lat = 0;
        double cur_long = 0;

        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (locationGPS == null)
        {
            cur_lat = locationNet.getLatitude();
        cur_long = locationNet.getLongitude();
        }
        else
        {
            cur_lat = locationGPS.getLatitude();
            cur_long = locationGPS.getLongitude();
        }
        GPSPathpoint testpoint = new GPSPathpoint();
        testpoint.setPointdatetime(new Date());
        testpoint.setPLatitude(cur_lat);
        testpoint.setPLongitude(cur_long);



        mMap.setMyLocationEnabled(true);

        mGoogleApiClient.connect();


         LatLng cur = new LatLng(cur_lat,cur_long);
         mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));


       TextView testtext =   (TextView)actmaps.findViewById(R.id.textView);
        testtext.setText(String.valueOf(cur_lat+" "+cur_long));
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(actmaps);
        dlgAlert.setMessage(String.valueOf(cur_lat));
        dlgAlert.setTitle("App Title");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }
    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
