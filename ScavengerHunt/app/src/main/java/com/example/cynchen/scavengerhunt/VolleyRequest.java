package com.example.cynchen.scavengerhunt;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
                        ArrayList<JSONObject> locationvideolist = new ArrayList<JSONObject>();
                        try {
                            //get items array, which we will parse to get all of the links
                            JSONArray items = response.getJSONArray("path");
                            for (int i=0; i<items.length(); i++){
                                JSONObject image_item = items.getJSONObject(i);
                                locationvideolist.add(image_item); //add each link to arraylist of image links
                            }
                        } catch (Exception e) {
                            Log.e("ERROR!", e.getMessage()); //catch error
                        }
                        callback.callback(locationvideolist); //use callback to return imagelinks arraylist
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR!", error.getMessage());
                        callback.callback(null); //don't callback anything if unsuccessful
                    }
                }
        );
        queue.add(request); //make the queue request
    }
}
