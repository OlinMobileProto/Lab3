package com.example.cynchen.scavengerhunt;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cynchen on 10/22/15.
 */
public interface searchcallback {
    //callback function created
    void callback(ArrayList<String> videos, ArrayList<String> longitudes, ArrayList<String> latitudes);
}
