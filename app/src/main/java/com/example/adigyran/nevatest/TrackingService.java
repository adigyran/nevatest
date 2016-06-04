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
import java.util.List;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AsyncResponse {
    private final IBinder TRKBinder = new LocalBinder();
    private  GPSPathlist gpsPathlist;
    private  GPSPathlist gpsPathlistR;
    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mMap = null;
    MapsActivity actmaps;
    GPSPathpoint firstpoints =null;
    GPSPathpoint secondpoints =null;
    List<String> pointdates = null;
    boolean pointsloaded = false;
    boolean isrecord =false;
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
                //for(GPSPathpoint pointr:output.getGPSPoints())
                //{

                   // LatLng mark = new LatLng(pointr.getPLatitude(),pointr.getPLongitude());
                   // Marker markerw = mMap.addMarker(new MarkerOptions().position(mark).draggable(false));

                //}
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
        if(this.isrecord)
        {
            this.isrecord=false;
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
                    this.gpsPathlist=null;
                }
            }
    }
    }
    public void Record(boolean isrecord, GoogleMap mMap) {
        pointsloaded =false;
        this.mMap = mMap;
        mMap.clear();
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
            this.isrecord=true;
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

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 20, gpsPathLocationListner);
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

    public void Chosenfirst(int chosenn)
    {
        if(!(pointdates==null)) {


            if (!(chosenn == -1)) {
                try {
                    firstpoints = new GPSPathpoint();
                    firstpoints.setPointdatetime(gpsPathlistR.getGPSPoints().get(chosenn).getPointdatetime());
                    for(int i=0;i<chosenn;i++)
                    {
                        pointdates.set(i,getString(R.string.choose_second));
                    }
                    actmaps.pointdatachoose(pointdates, "Choose second point", false);
                } catch (NullPointerException e) {

                }

            }
        }
    }
    public void Chosensecond(int chosenn)
    {
        if(!(pointdates==null)) {


            if (!(chosenn == -1)) {
                try {
                    secondpoints = new GPSPathpoint();
                    secondpoints.setPointdatetime(gpsPathlistR.getGPSPoints().get(chosenn).getPointdatetime());
                } catch (NullPointerException e) {

                }

            }
            if(firstpoints == null)
            {
                firstpoints = new GPSPathpoint();
                firstpoints.setPointdatetime(gpsPathlistR.getGPSPoints().get(0).getPointdatetime());

            }
            if(secondpoints ==null)
            {
                secondpoints = new GPSPathpoint();
                secondpoints.setPointdatetime(gpsPathlistR.getGPSPoints().get(gpsPathlistR.getGPSPoints().size()).getPointdatetime());
            }
           if(!(firstpoints==null) && !(secondpoints==null)) {
               GPSPathlist testlist = new GPSPathlist();
               testlist.setGPSPoints(gpsPathlistR.getrange(firstpoints, secondpoints,gpsPathlistR));
                if(!(testlist.getGPSPoints()==null)) {
                    mMap.clear();
                    for (GPSPathpoint readpoint : testlist.getGPSPoints()) {
                        Log.d("nevatest", "Chosensecond: " + GPSPathutility.DateToString(readpoint.getPointdatetime()));
                        LatLng mark = new LatLng(readpoint.getPLatitude(),readpoint.getPLongitude());
                        Marker markerw = mMap.addMarker(new MarkerOptions().position(mark).draggable(false).title(String.valueOf(GPSPathutility.DateToString(readpoint.getPointdatetime()))));

                    }
                }
           }
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
                    pointdates = new ArrayList<String>();
                    Log.d("nevatest", "doInBackground: " + String.valueOf(gpsPathlistR.getGPSPoints().size()));
                    for(GPSPathpoint readpoint:gpsPathlistR.getGPSPoints())
                    {
                        Log.d("nevatest", "TimeChooseDialog: "+GPSPathutility.DateToString(readpoint.getPointdatetime()));
                        pointdates.add(GPSPathutility.DateToString(readpoint.getPointdatetime()));
                    }

                    actmaps.pointdatachoose(pointdates,"Choose first point",true);
                    //int chosenf =  actmaps.pointdatachoose(pointdates,"Choose first point");
                   // int chosens = actmaps.pointdatachoose(pointdates,"Choose second point");
                    //if(!(chosenf==-1))
                   // {
                     //   try{
                      //  firstpoints = new GPSPathpoint();
                       //     firstpoints.setPointdatetime(GPSPathutility.StringToDate(pointdates.get(chosenf)));
                       // }
                       // catch (NullPointerException e)
                       // {

                   //     }

                    //}
                   // if(!(chosens==-1))
                   // {
                    //    try{
                     //       secondpoints = new GPSPathpoint();
                    //        secondpoints.setPointdatetime(GPSPathutility.StringToDate(pointdates.get(chosens)));
                     //   }
                     //   catch (NullPointerException e)
                     //   {

                      //  }

                    //}
                   // if(!(firstpoints==null) && !(secondpoints==null)){
                   // GPSPathlist testlist = new GPSPathlist();
                   // testlist.setGPSPoints(gpsPathlistR.getrange(firstpoints, secondpoints));
                   // for (GPSPathpoint readpoint : testlist.getGPSPoints()) {
                   //     Log.d("nevatest", "TimeChooseDialog: " + GPSPathutility.DateToString(readpoint.getPointdatetime()));
                        // pointdates.add(GPSPathutility.DateToString(readpoint.getPointdatetime()));
                   // }


                }
            }
            //pointsloaded=false;
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
