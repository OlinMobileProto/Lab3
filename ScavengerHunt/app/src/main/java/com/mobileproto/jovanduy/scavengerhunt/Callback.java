package com.mobileproto.jovanduy.scavengerhunt;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Jordan on 10/17/15.
 */
public interface Callback {
    public void callback(boolean success, double lat, double longi, String vid, boolean isLast);
}