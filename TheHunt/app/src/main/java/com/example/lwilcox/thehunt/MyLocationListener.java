package com.example.lwilcox.thehunt;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Location Listener: A listener for your location. If location services are enabled, whenever you move your position is logged
 */

public class MyLocationListener implements LocationListener {
    private Context context;
    private double[] cluePosition = new double[2]; //location of the clue -> its long, lat
    private double[] startPosition = new double[2]; //the location of the previous clue (if first clue, your start location)
    private double[] yourPosition = new double[2]; //location of you
    private boolean locationFound = false;
    VideoFragment fragment;
    private Boolean setInitialPosition = false; //to set the initial position you are at when you start a new clue. This is for changing the background color
    private double within_dist = .00005; //TODO: change this to be a bit more exact. currently this makes a 5.6 m radius
    private double ratio = 1;
    private double b;
    private double c;
    private int color;
    private String hex;

    public MyLocationListener (Context mContext, VideoFragment mfragment){
        fragment = mfragment;
        context = mContext;
        this. cluePosition[0] = 0;
        this. cluePosition[1] = 0;
    }

    public void setCluePosition(double lon, double lat){
        this. cluePosition[0] = lon;
        this. cluePosition[1] = lat;
    }

    @Override
    public void onLocationChanged(Location location) {
        yourPosition[0] = location.getLatitude();
        yourPosition[1] = location.getLongitude();
        Log.d("Start position is.", "set_initial_pos = " + setInitialPosition);

        if(setInitialPosition == false){ //to set your start position for the first clue
            startPosition[0] = yourPosition[0];
            startPosition[1] = yourPosition[1];
            setInitialPosition = true; //should never be false again afterwards
            Log.d("Start position set", "set_initial_pos = " + setInitialPosition);
        }

        Log.d("Location", yourPosition[0] + ", " + yourPosition[1]);
        checkLocation();
        if (locationFound == false){
            // make button unclickable if you aren't at the location/were there then left
            fragment.dontSetCameraButton();
        } else if (locationFound == true){
            // prompt to take picture, then upload, then download clue
            fragment.setCameraButton();
            locationFound = false;
        }
    }

    //if you've taken a picture and you're done with the clue
    public void doneWithClue(){
        //set new initial positions for finding next clue
        Log.d("Done with clue", "going to reset start_position values to your_position");
        startPosition[0] = yourPosition[0];
        startPosition[1] = yourPosition[1];
    }

    //checks to see if you are within location
    public void checkLocation(){
        changeBackground();
        //if you are within radius
        if (yourPosition[0] >=  cluePosition[0] - within_dist || yourPosition[0] <=  cluePosition[0] + within_dist ||
                yourPosition[1] >=  cluePosition[1] - within_dist || yourPosition[1] <=  cluePosition[1] + within_dist) {
            locationFound = true;
        }
    }

    //changes the color of the background as you move
    public void changeBackground(){
        b = distanceFormula( cluePosition, yourPosition);
        c = distanceFormula( cluePosition, startPosition);
        ratio = b/c;
        Log.d("Start position", "" + startPosition[0] + ", " + startPosition[1]);
        Log.d("Ratio???", "is " + ratio + ", b: " + b + ", c: " + c);
        if(ratio >= 1){ //makes background white if not close enough
            fragment.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else{ //otherwise, changes color
            color = (int) Math.round(ratio * 256);
            hex = Integer.toHexString(color);
            Log.d("Color", "#" + hex + "FF" + hex);
            fragment.relativeLayout.setBackgroundColor(Color.parseColor("#" + hex + "FF" + hex));
        }
    }

    private double distanceFormula(double[] a, double[] b){
        return Math.sqrt(((b[0] - a[0])*(b[0] - a[0])) + ((b[1] - a[1])*(b[1] - a[1])));
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