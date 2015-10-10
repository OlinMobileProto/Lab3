package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by matt on 10/8/15.
 */
public class GpsHandler {
    public double latitude;
    public double longitude;
    private LocationListener locationListener;
    private LocationManager locationManager;


    public GpsHandler(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void passGpsCallback(GpsCallback callback) {
        final GpsCallback finalizedCallback = callback;
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                finalizedCallback.callback(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException ex) {
            Log.e("SECURITY_ERROR", ex.getMessage());
            Log.i("PRINTER", "If this gets called it means that the GPS  permissions didn't work or something.");
        }
    }


}
