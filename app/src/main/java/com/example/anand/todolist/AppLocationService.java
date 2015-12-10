package com.example.anand.todolist;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class AppLocationService extends Service implements LocationListener {

    protected LocationManager locationManager;
    Location location;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    public AppLocationService(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation(String provider) {
        try {
            if (locationManager.isProviderEnabled(provider)) {
                locationManager.requestLocationUpdates(provider,
                        MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    return location;
                }
            }
        }
        try{gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
        catch(SecurityException e)
        {e.printStackTrace();}
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}

    void getMyCurrentLocation() {

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();
        try{gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
        //don't start listeners if no provider is enabled

        //if(!gps_enabled && !network_enabled)

        //return false;

        if(gps_enabled){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if(gps_enabled){
            location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        if(network_enabled && location==null){

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }
        if(network_enabled && location==null)    {
            location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();
        } else {
            Location loc= getLastKnownLocation(this);
            if (loc != null) {
                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();
            }
