package com.mobileproto.jovanduy.scavengerhunt;


/**
 * Interface for callback for Volley HTTP request to post an image
 */
public interface PutCallback {
    public void callbackPut(boolean success, String statusCode);
}