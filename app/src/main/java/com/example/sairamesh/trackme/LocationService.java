package com.example.sairamesh.trackme;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sairamesh on 2/10/17.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private DatabaseReference mDatabase;
    private String TAG="Location Service";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"starting service");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"Location service started");
        mDatabase= FirebaseDatabase.getInstance().getReference("UsersSharing").child(Registration.uid).child("location");
        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
        //}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest,this);
        }
        Log.i(TAG,"requestLocationUpdates started");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(getApplicationContext(),"New location is obtained",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Latitude"+location.getLatitude()+"\t Longitude"+location.getLongitude(),Toast.LENGTH_SHORT).show();
        mDatabase.setValue(location.getLatitude()+";"+location.getLongitude()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.i(TAG, "Location Update:Written location to firebase");
                    //Toast.makeText(getApplicationContext(),"Location Update:Written location to firebase",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i(TAG, "Location Update:Problem in writing location to firebase");
                    //Toast.makeText(getApplicationContext(),"Location Update:Problem in writing location to firebase",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected synchronized void buildGoogleApiClient(){
        client=new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
        Log.i(TAG,"Client connected");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Service is getting destroyed",Toast.LENGTH_SHORT).show();

    }
}
