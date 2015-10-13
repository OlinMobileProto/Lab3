package com.example.lwilcox.thehunt;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nmohamed on 10/13/2015.
 */
public class MyLocationListener implements LocationListener {
    private Context context;
    public double[] position = new double[2];
    public boolean locationFound = false;

    public MyLocationListener (Context mContext){
        context = mContext;
    }

    @Override
    public void onLocationChanged(Location location) {
        position[0] = location.getLongitude();
        position[1] = location.getLatitude();
        Log.d("Location", position[0] + ", " + position[1]);
        checkLocation();
    }

    public void checkLocation(){
        //use position[0], position[1]
        //TODO: write function that checks to see if you are within 10M of clue location

        //relativeLayout.setBackgroundColor(Color.GREEN);
        locationFound = false; //true if you are within distance
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(context, "Provider status changed",
                Toast.LENGTH_LONG).show();
    }
    public void onProviderEnabled(String s) {
        Toast.makeText(context, "Provider enabled by the user. GPS turned on",
                Toast.LENGTH_LONG).show();
    }
    public void onProviderDisabled(String s){
        Toast.makeText(context, "Provider disabled by the user. GPS turned off",
                Toast.LENGTH_LONG).show();
    }
}