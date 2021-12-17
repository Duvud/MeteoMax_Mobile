package com.example.euskalmet.Location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class LocationRequest implements LocationListener {
    @SuppressLint("StaticFieldLeak")
    private static LocationRequest locationRequest;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private LocationManager locationManager;
    public double latitude, longitude;
    public boolean locationInited = false;

    public LocationRequest(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public static LocationRequest getLocationRequest(Context context) {
        if(locationRequest == null) {
            locationRequest = new LocationRequest(context);
        }
        return  locationRequest;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("Location",location.getLatitude() + " - " + location.getLongitude());
        locationInited = true;
    }
}
