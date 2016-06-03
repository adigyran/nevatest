package com.example.adigyran.nevatest;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AsyncResponse {
    private final IBinder TRKBinder = new LocalBinder();
    private  GPSPathlist gpsPathlist;
    private  GPSPathlist gpsPathlistR;
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mMap = null;
    MapsActivity actmaps;
    boolean pointsloaded = false;
    GPSPathLocationListner gpsPathLocationListner = null;



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

    @Override
    public void processFinish(GPSPathlist output) {
        if(!(output==null)&&!(mMap==null))
        {
            if(!(output.getGPSPoints()==null))
            {
                gpsPathlistR = output;
                pointsloaded = true;
                mMap.clear();
                //output.getrange()
                for(GPSPathpoint pointr:output.getGPSPoints())
                {

                    LatLng mark = new LatLng(pointr.getPLatitude(),pointr.getPLongitude());
                    Marker markerw = mMap.addMarker(new MarkerOptions().position(mark).draggable(false));

                }
            }
        }
    }

    public class LocalBinder extends Binder {
        TrackingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackingService.this;
        }
    }
    public void Play(GoogleMap mMap)
    {
        this.mMap = mMap;
        GPSPathDbghelper helper = new GPSPathDbghelper(getApplicationContext());
        GPSReadTask testrd = new GPSReadTask(helper,gpsPathlist);
        testrd.delegate = this;
        testrd.execute();
    }
    public void StopRecording(boolean isrecord)
    {

        Log.i("Ff", "StopRecording: ");
        if(isrecord)
        {
            isrecord=false;
            if(!(gpsPathLocationListner==null))
            {
                gpsPathLocationListner.setRecording(false);
                gpsPathlist=gpsPathLocationListner.getLoclistpl();

            }
            if(!(gpsPathlist==null)) {
                if (!(gpsPathlist.getGPSPoints() == null)) {
                    TextView testtext = (TextView) actmaps.findViewById(R.id.textView);
                    testtext.setText(String.valueOf(gpsPathlist.getGPSPoints().size()));
                    Log.d("nevatest", "doInBackground: " + String.valueOf(gpsPathlist.getGPSPoints().size()));
                    GPSPathDbghelper helper = new GPSPathDbghelper(getApplicationContext());
                    GPSWriteTask testwr = new GPSWriteTask(helper, gpsPathlist);
                    //gpsPathlist.removeGPSPoints(gpsPathlist.getGPSPoints());
                    testwr.execute();
                }
            }
    }
    }
    public void Record(boolean isrecord, GoogleMap mMap) {
        pointsloaded =false;
        this.mMap = mMap;
        double cur_lat = 0;
        double cur_long = 0;
        if(isrecord) {
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

            if (gpsPathlist == null) {
                gpsPathlist = new GPSPathlist();
            }
                Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(gpsPathLocationListner==null) {
                gpsPathLocationListner = new GPSPathLocationListner();
            }
            mMap.setMyLocationEnabled(true);

             TextView testtext = (TextView) actmaps.findViewById(R.id.textView);
            //   testtext.setText(String.valueOf(cur_lat + " " + cur_long));
            mGoogleApiClient.connect();
            gpsPathLocationListner.setLmMap(mMap);
            gpsPathLocationListner.setLoclistpl(gpsPathlist);
            gpsPathLocationListner.setCoordtext(testtext);
            gpsPathLocationListner.setRecording(isrecord);
            gpsPathLocationListner.setActmapsl(actmaps);
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, gpsPathLocationListner);
                //if (locationGPS == null) {
                 //   cur_lat = locationNet.getLatitude();
                //    cur_long = locationNet.getLongitude();
               // } else {
                 //   cur_lat = locationGPS.getLatitude();
                //    cur_long = locationGPS.getLongitude();
               // }
               // GPSPathpoint testpoint = new GPSPathpoint();
              //  testpoint.setPointdatetime(new Date());
            //    testpoint.setPLatitude(cur_lat);
             //   testpoint.setPLongitude(cur_long);


               // gpsPathlist.addGPSPoint(testpoint);
                //Log.d("nevatest", "doInBackground: "+String.valueOf(i));





           // if(!(gpsPathlist.getGPSPoints()==null)) {
            //    LatLng cur = new LatLng(gpsPathlist.getGPSPoints().get(gpsPathlist.getGPSPoints().size() - 1).getPLatitude(), gpsPathlist.getGPSPoints().get(gpsPathlist.getGPSPoints().size() - 1).getPLongitude());
            //    mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));


           //     TextView testtext = (TextView) actmaps.findViewById(R.id.textView);
             //   testtext.setText(String.valueOf(cur_lat + " " + cur_long));
           // }
          //  final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(actmaps);
           // dlgAlert.setMessage(String.valueOf(cur_lat));
            //dlgAlert.setTitle("App Title");
            //dlgAlert.setPositiveButton("OK", null);
            //dlgAlert.setCancelable(true);
            //dlgAlert.create().show();
            //dlgAlert.setPositiveButton("Ok",
             //       new DialogInterface.OnClickListener() {
              //          public void onClick(DialogInterface dialog, int which) {
               //         }
                //    });
        }
    }

    public void TimeChooseDialog()
    {
        if(pointsloaded)
        {
            if(!(gpsPathlistR==null))
            {
                if(!(gpsPathlistR.getGPSPoints()==null))
                {
                    ArrayList<String> pointdates = null;
                    for(GPSPathpoint readpoint:gpsPathlistR.getGPSPoints())
                    {
                        pointdates.add(GPSPathutility.DateToString(readpoint.getPointdatetime()));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, pointdates);
                    AlertDialog.Builder builder = new AlertDialog.Builder(actmaps);
                    builder.setTitle("Choose first date").setAdapter(adapter,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                        }
                    });


                }
            }
            pointsloaded=false;
        }
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
