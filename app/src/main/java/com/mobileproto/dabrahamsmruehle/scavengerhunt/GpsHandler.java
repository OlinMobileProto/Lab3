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
    private GpsCallback callback;

    private LocationListener locationListener = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            callback.callback(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    private LocationManager locationManager;



    public GpsHandler(Context context, final GpsCallback cb) {
        this.callback = cb;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException ex) {
            Log.e("SECURITY_ERROR", ex.getMessage());
        }
    }
}
