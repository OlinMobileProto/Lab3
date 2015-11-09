package com.example.jong.savangerhunt_1;

/**
 * Created by root on 10/27/15.
 */
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class S3service {
    private static JSONArray pathJSON=null;
    private static RequestQueue queue;
    private static String url;



    // Constructor
    public S3service(Context context){
        queue = Volley.newRequestQueue(context);
        url = "http://45.55.65.113/scavangerhunt";
        // Request a JSON response from the url
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, future,future);
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
//        try {
//            pathJSON=future.get(15, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
    }


    public String getVideoURLForStage(int stage) {
        //for testing purpose
        String[] vidlist ={"MVI_3146.3gp","MVI_3145.3gp","MVI_3144.3gp","MVI_3147.3gp","MVI_3141.3gp","MVI_3147.3gp"};
        String videoID ="";
        try {
            if(pathJSON!=null){
                videoID=pathJSON.getJSONObject(stage).get("s3id").toString();
            }
            else{
                videoID= vidlist[stage-1];
            }
            return "https://s3.amazonaws.com/olin-mobile-proto/"+videoID;
        } catch (JSONException e) {
            e.printStackTrace();
            return "https://s3.amazonaws.com/olin-mobile-proto/"+videoID;
        }
    }
    public String getLatitudeForStage(int stage) {
        //for testing purpose
        String latitude="";
        String[] latitudes={"42.29386","42.292987","42.292733","42.293445","42.293108","42.292701"};
        try {
            if(pathJSON!=null){
                latitude=pathJSON.getJSONObject(stage).get("latitude").toString();
            }
            else{
                latitude= latitudes[stage-1];
            }
            return latitude;
        } catch (JSONException e) {
            e.printStackTrace();
            return latitude;
        }
    }

    public String getLongitudeForStage(int stage) {
        //for testing purpose
        String longitude="";
        String [] longitudes = {"-71.26483","-71.264039","-71.263977","-71.263481","-71.262802","-71.262054"};
        try {
            if(pathJSON!=null){
                longitude= pathJSON.getJSONObject(stage).get("longitude").toString();
            }
            else{
                longitude=longitudes[stage-1];
            }
            return longitude;

        } catch (JSONException e) {
            e.printStackTrace();
            return longitude;
        }
    }
}
