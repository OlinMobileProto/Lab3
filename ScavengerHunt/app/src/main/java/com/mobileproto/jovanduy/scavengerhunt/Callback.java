package com.mobileproto.jovanduy.scavengerhunt;


/**
 * Interface to be used to implement callbacks in Volley HTTP
 * requests to GET information about the scavenger hunt
 */
public interface Callback {
    public void callback(boolean success, double lat, double longi, String vid, boolean isLast);
}