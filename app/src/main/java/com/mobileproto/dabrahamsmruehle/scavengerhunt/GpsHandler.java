package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;



/**
 * Created by matt on 10/8/15.
 */
public class GpsHandler {

    public double latitude;
    public double longitude;
    private GpsCallback callback;
    private LocationListener locationListener;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;


    public GpsHandler(Context context, final GpsCallback cb) {
        locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        callback = cb;
        Log.d("GpsVals", "Set callback; about to start locationListener");
        locationListener = new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                    {
                        Log.d("GpsVals", "onLocationChanged being called");
                        callback.callback(location);
                    }
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Log.d("GpsVals", "requested location updates with locationListener");
        } catch (SecurityException ex) {
            Log.e("SECURITY_ERROR", ex.getMessage());
        }
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }
}
