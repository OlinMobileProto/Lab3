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
    public double[] position = new double[2]; //location of the clue
    public double[] yourPosition = new double[2]; //location of you
    public boolean locationFound = false;
    VideoFragment fragment;

    public MyLocationListener (Context mContext, VideoFragment mfragment){
        fragment = mfragment;
        context = mContext;
        this.position[0] = 0;
        this.position[1] = 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        yourPosition[0] = location.getLongitude();
        yourPosition[1] = location.getLatitude();
        Log.d("Location", yourPosition[0] + ", " + yourPosition[1]);
        checkLocation();
        if (locationFound == true){
            // prompt to take picture, then upload, then download clue
            fragment.setCameraButton(); //i don't think this is correct
            locationFound = false;
        }
    }

    public void checkLocation(){
        int within_dist = 10; //change this
        //relativeLayout.setBackgroundColor(Color.GREEN);

        if (yourPosition[0] >= position[0] - within_dist || yourPosition[0] <= position[0] + within_dist ||
                yourPosition[1] >= position[1] - within_dist || yourPosition[1] <= position[1] + within_dist) {
            locationFound = true;
        }
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