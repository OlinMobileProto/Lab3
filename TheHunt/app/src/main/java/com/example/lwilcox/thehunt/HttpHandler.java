package com.example.lwilcox.thehunt;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.media.Image;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * HTTP Handler: A class that handles Http requests. Gets clue information from a SQL database
 */

public class HttpHandler {
    public RequestQueue queue;
    public HttpHandler(Context context) {
        queue = Volley.newRequestQueue(context);
    }
    public void getClues(final Callback callback) {
        String URL = "http://45.55.65.113/scavengerhunt";

        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        ArrayList<Integer> clueIds = new ArrayList<>();
                        ArrayList<Integer> clueLats = new ArrayList<>();
                        ArrayList<Integer> clueLongs = new ArrayList<>();
                        ArrayList<String> clueS3ids = new ArrayList<>();
                        try {
                            JSONArray paths = response.getJSONArray("path");
                            for (int i = 0; i < paths.length(); i++) {
                                JSONObject path = paths.getJSONObject(i);
                                Integer id = path.getInt("id");
                                Integer latitude = path.getInt("latitude");
                                Integer longitude = path.getInt("longitude");
                                String s3id = path.getString("s3id");
                                clueIds.add(id);
                                clueLats.add(latitude);
                                clueLongs.add(longitude);
                                clueS3ids.add(s3id);
                            }
                                callback.callback(clueIds, clueLats, clueLongs, clueS3ids);
                        } catch (Exception e)
                        {
                            Log.d("Failure", "Not from JSON Object");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)  {
                        Log.e("Error", error.getMessage());
                    }
                }
        );

        queue.add(getRequest);
    }

}
