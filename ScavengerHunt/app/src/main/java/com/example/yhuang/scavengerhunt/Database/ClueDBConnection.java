package com.example.yhuang.scavengerhunt.Database;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class establishes connection to the server,
 * HTTP request and get JSON information using Volley.
 * Maps each row to an id for easy access
 */

public class ClueDBConnection {
    RequestQueue searchQueue;
    RequestQueue postQueue;
    RequestQueue locationQueue;
    private final static String url = "http://45.55.65.113/scavengerhunt"; //url for JSON data
    private final static String appUrl = "http://45.55.65.113/userdata/rabbit"; //url for posting and getting all the data

    public ClueDBConnection(Context context) {
        //Request queue for search
        searchQueue = Volley.newRequestQueue(context);
        postQueue = Volley.newRequestQueue(context);
        locationQueue = Volley.newRequestQueue(context);
    }

    public void getLocations(final CallbackInterface callback) {
        final ArrayList<Double> latitudeList = new ArrayList<>(); //Latitude info in list form
        final ArrayList<Double> longitudeList = new ArrayList<>() ;//Longitude info in list form
        final ArrayList<Integer> idList = new ArrayList<>(); //Place number
        final ArrayList<String> s3vid = new ArrayList<>(); //Amazon S3 clue videos id

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //AsyncTask for getting JSON
                        try {
                            //Filtering and looping to get/sort JSON
                            JSONArray urlJson = response.getJSONArray("path");
                            for(int i = 0; i < urlJson.length(); i++){
                                    latitudeList.add(urlJson.getJSONObject(i).getDouble("latitude"));
                                    longitudeList.add(urlJson.getJSONObject(i).getDouble("longitude"));
                                    idList.add(urlJson.getJSONObject(i).getInt("id"));
                                    s3vid.add(urlJson.getJSONObject(i).getString("s3id"));
                            }
                            //Map  of the index to the Row object
                            Map<Integer, ClueRow.Row> map = ClueRow.Map(idList,latitudeList,longitudeList,s3vid);
                            callback.resultsCallback(map);
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                    }
                }
        );
        searchQueue.add(request);
    }

    public void postIds(String imageKey, String imageLocation) {
        JSONObject bodyInfo = new JSONObject();
        try {
            bodyInfo.put("imageKey", imageKey);
            bodyInfo.put("imageLocation", imageLocation);
        }catch (Exception e) {
            Log.e("JSON PUT", e.getMessage());
        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                appUrl,
                bodyInfo,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //continue;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.getMessage());
                    }
                }
        );
        postQueue.add(request);
    }

    public void getIds(final LocationInterface location, final int imageLocation) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                appUrl,
                new JSONObject(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Filtering and looping to get/sort JSON
                            JSONArray urlJson = response.getJSONArray("data");
                            for (int i=0; i<urlJson.length(); i++) {
                                if (urlJson.getJSONObject(i).getInt("imageKey") == imageLocation){
                                    String locationImage = urlJson.getJSONObject(i).getString("imageLocation");
                                    location.locationCallback(locationImage);
                                }
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                    }
                }
        );
        locationQueue.add(request);
    }

}