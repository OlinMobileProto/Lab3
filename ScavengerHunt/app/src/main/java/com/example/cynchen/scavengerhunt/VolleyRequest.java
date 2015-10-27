package com.example.cynchen.scavengerhunt;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by cynchen on 10/22/15.
 */
public class VolleyRequest {
    public RequestQueue queue;

    //Makes the volley request
    public VolleyRequest (Context context){
        queue = Volley.newRequestQueue(context);
    }

    //Function that searches the input to the function, given by SearchFragment on search click
    public void getlocations(final searchcallback callback) {
//        String query = searchQuery.replaceAll(" ", "+"); //replaces spaces with +
        String url = "http://45.55.65.113/scavengerhunt";

        //Requesting the JSON object with the custom search url
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Initializes an arraylist that we will fill with image url's
                        ArrayList<String> locationvideolist = new ArrayList<String>();
                        ArrayList<String> longitudes = new ArrayList<String>();
                        ArrayList<String> latitudes = new ArrayList<String>();
                        try {
                            //get items array, which we will parse to get all of the links
                            JSONArray items = response.getJSONArray("path");
                            for (int i=0; i<items.length(); i++){
                                JSONObject image_item = items.getJSONObject(i);
                                String videos = "https://s3.amazonaws.com/olin-mobile-proto/" + image_item.getString("s3id");
                                String longitude = image_item.getString("longitude");
                                String latitude = image_item.getString("latitude");
                                locationvideolist.add(videos); //add each link to arraylist of image links
                                longitudes.add(longitude);
                                latitudes.add(latitude);
                            }
                        } catch (Exception e) {
                            Log.e("ERROR!", e.getMessage()); //catch error
                        }
                        callback.callback(locationvideolist, longitudes, latitudes); //use callback to return imagelinks arraylist
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR!", error.getMessage());
                        callback.callback(null, null, null); //don't callback anything if unsuccessful
                    }
                }
        );
        queue.add(request); //make the queue request
    }

    public void putID(final UUID uid, int location){
        String url = "http://45.55.65.113/userdata/CynthiaZoherMobProto2015Lab3";
        JSONObject ImageInfo = new JSONObject();
        try{
            ImageInfo.put("imageKey", uid.toString());
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("Location: ", String.valueOf(location));
        try{
            ImageInfo.put("imageLocation", String.valueOf(location));
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("JSONObject: ", ImageInfo.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                ImageInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);

    }
}
