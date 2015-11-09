package com.mobileproto.jovanduy.scavengerhunt;

import android.util.Log;

/**
 * Created by root on 10/22/15.
 */
public class CalculateLocation {
    private double lat1;
    private double lon1;
    private double lat2;
    private double lon2;

    public CalculateLocation(){

    }
    private String TAG = "CalculatLocation";
    public double calculateDistance(String slat1, String slon1, String slat2, String slon2){
        lat1 = Double.parseDouble(slat1);
        lon1 = Double.parseDouble(slon1);
        lat2 = Double.parseDouble(slat2);
        lon2 = Double.parseDouble(slon2);
        double distance;


        Double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;

        distance = (d * 1000);
        Log.d(TAG, "distance:"+distance);
        return distance; // meters
    }

    public String calculateDirection(){
        String direction ="";
        Double latitudeOffset = lat1 - lat2;
        Double longitudeOffset = lon1 - lon2;
        if (longitudeOffset < 0){
            if (latitudeOffset < 0){
                direction = "NE";
            }
            else if(latitudeOffset >0){
                direction = "SE";
            }
            else if(latitudeOffset == 0){
                direction = "E";
            }
        }

        else if (longitudeOffset>0) {
            if (latitudeOffset < 0) {
                direction = "NW";
            } else if (latitudeOffset > 0) {
                direction = "SW";
            } else if (latitudeOffset == 0) {
                direction = "W";
            }
        }
        else if (longitudeOffset == 0){
            if (latitudeOffset < 0){
                direction = "N";
                }
            else if(latitudeOffset >0){
                direction = "S";
            }
        }


        return direction;
    }
}
