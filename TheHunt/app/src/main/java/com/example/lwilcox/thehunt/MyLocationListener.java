package com.example.lwilcox.thehunt;

import android.content.Context;
import android.graphics.Color;
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
    public double[] clue_position = new double[2]; //location of the clue -> its long, lat
    private double[] start_position = new double[2]; //the location of the previous clue (if first clue, your start location)
    public double[] your_position = new double[2]; //location of you
    public boolean locationFound = false;
    VideoFragment fragment;
    private Boolean set_initial_pos = false; //to set the initial position you are at when you start a new clue. This is for changing the background color
    public boolean tookPic = false;
    public double ratio = 1;

    public MyLocationListener (Context mContext, VideoFragment mfragment){
        fragment = mfragment;
        context = mContext;
        this.clue_position[0] = 0;
        this.clue_position[1] = 0;
    }

    public double[] getCluePosition(){
        return this.clue_position; //finish this later by making stuff private and changing functions in videofragment
    }

    public void setCluePosition(double lon, double lat){
        this.clue_position[0] = lon;
        this.clue_position[1] = lat; //finish this later by making stuff private and changing functions in videofragment
    }

    @Override
    public void onLocationChanged(Location location) {
        your_position[0] = location.getLongitude();
        your_position[1] = location.getLatitude();
        Log.d("Start position is.", "set_initial_pos = " + set_initial_pos);

        if(set_initial_pos == false){ //to set your start position for the first clue
            start_position[0] = your_position[0];
            start_position[1] = your_position[1];
            set_initial_pos = true; //should never be false again afterwards
            Log.d("Start position set", "set_initial_pos = " + set_initial_pos);
        }

        Log.d("Location", your_position[0] + ", " + your_position[1]);
        checkLocation();
        if (locationFound == true){
            // prompt to take picture, then upload, then download clue
            fragment.setCameraButton();
            locationFound = false;
        }
    }

    public void doneWithClue(){
        //checks to see if you took a picture, so you know
        tookPic = true; //do something with this later
        //set new initial positions for finding next clue
        Log.d("Done with clue", "going to reset start_position values to your_position");
        start_position[0] = your_position[0];
        start_position[1] = your_position[1];
    }

    public void checkLocation(){
        double within_dist = .00005; //change this to be a bit more exact. currently this makes a 5.6 m radius

        changeBackground();

        if (your_position[0] >= clue_position[0] - within_dist || your_position[0] <= clue_position[0] + within_dist ||
                your_position[1] >= clue_position[1] - within_dist || your_position[1] <= clue_position[1] + within_dist) {
            locationFound = true;
        }
    }

    public void changeBackground(){
        int color;
        String hex;
        double b =distanceFormula(clue_position, your_position);
        double c =distanceFormula(clue_position, start_position);
        ratio = b/c;
        Log.d("Start position", "" + start_position[0] + ", " + start_position[1]);
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
        return Math.sqrt(((b[0] - a[0])*(b[0] - a[0])) + ((b[1] - a[1])*(b[1] - a[1]))); //TODO: fix this so it's
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