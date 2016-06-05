package com.example.adigyran.nevatest;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //private static final int MY_LOCATION_REQUEST_CODE = ;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient = null;
    private TrackingService Tracking = null;
    private CountDownTimer GPSTimer = null;
    private boolean isrecord = false;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TrackingService.LocalBinder binder = (TrackingService.LocalBinder) service;
            Tracking = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GPSPathDbghelper gpspath = new GPSPathDbghelper(getApplicationContext());


       // set

       // if (mGoogleApiClient == null) {
        //    mGoogleApiClient = new GoogleApiClient.Builder(this)
         //           .addConnectionCallbacks(this)
          //          .addOnConnectionFailedListener(this)
           //         .addApi(LocationServices.API)
         //           .build();
       // }
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings settingsm =  mMap.getUiSettings();
        settingsm.setMyLocationButtonEnabled(true);
        settingsm.setZoomControlsEnabled(true);

        Intent Trackingintent  = new Intent(this,TrackingService.class);
        startService(Trackingintent);
        bindService(Trackingintent,mConnection,Context.BIND_AUTO_CREATE);
//        Tracking.setActmaps(this);
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

           // mGoogleApiClient.connect();
           // Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
           // double cur_lat = mLastLocation.getLatitude();
           // double cur_long = mLastLocation.getLongitude();
            //LatLng cur = new LatLng(cur_lat,cur_long);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
        } else {
            // Show rationale and request permission.
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {

        Log.d("nevatest", "onStop: ");
        super.onStop();
        //mGoogleApiClient.disconnect();
       // if (mBound)
        //{
         //   Tracking.StopRecording(true);

           // unbindService(mConnection);
           // mBound = false;
        //}
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    public int chosenpoint =-1;
    public int pointdatachoose(List<String> inptlist,String titlet,boolean frist)
    {
        //int chosen = -1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, inptlist);
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        if(frist) {
            builder.setTitle(titlet).setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    Tracking.Chosenfirst(which);
                }
            });
        }
        else
        {
            builder.setTitle(titlet).setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    Tracking.Chosensecond(which);
                }
            });
        }
        builder.create().show();
        return chosenpoint;
    }
    public int secondpoint =-1;

    private void startstoprec(boolean start){

        if(!start && isrecord)
        {
            isrecord=false;

        }
        if(start && !isrecord)
        {
            isrecord = true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Tracking.setActmaps(this);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_start_rec:
                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }
                else {
                    //recordnotif();
                    Tracking.Record(true, mMap);
                }
                return true;
            case R.id.action_stop_rec:
                Tracking.StopRecording(true);
                return true;
            case R.id.action_play_rec:
                Tracking.Play(mMap);
                return true;
            case R.id.action_remove_base:
               this.deleteDatabase(GPSPathDbghelper.DATABASE_NAME);
                return true;
            case R.id.action_choise_time:
                Tracking.TimeChooseDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
