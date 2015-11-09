package com.mobileproto.jovanduy.scavengerhunt;

/**
 * Interface for callback from Volley HTTP request to GET image information
 */
public interface GetImageCallback {
    public void callback(boolean success, String imageKey);
}