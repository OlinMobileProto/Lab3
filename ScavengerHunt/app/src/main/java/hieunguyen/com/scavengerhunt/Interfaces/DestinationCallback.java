package hieunguyen.com.scavengerhunt.Interfaces;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by hieunguyen on 10/17/15.
 */
public interface DestinationCallback {
    void destCallback(boolean success, JSONArray locations);
}
