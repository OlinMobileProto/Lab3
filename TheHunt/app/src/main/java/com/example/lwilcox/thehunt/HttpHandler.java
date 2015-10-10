package com.example.lwilcox.thehunt;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import android.media.Image;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import javax.security.auth.callback.Callback;

/**
 * Created by lwilcox on 10/8/2015.
 */
//TODO: do SQL database :'-(
//TODO: fix HttpHandler and do Volley stuff
public class HttpHandler {
        public RequestQueue queue;
    public HttpHandler(Context context) {
        queue = Volley.newRequestQueue(context);
    }
    public void imageSearch(final Callback callback) {
        String URL = "https://www.googleapis.com/customsearch/v1?cx=015805936300530222953:xfn9wvqvajy&key=AIzaSyBHSXnNE-tEICkyVFO_dgktm1sLbmXxwPw&";

        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {

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
