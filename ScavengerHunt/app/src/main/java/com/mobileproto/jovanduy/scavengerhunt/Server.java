package com.mobileproto.jovanduy.scavengerhunt;

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

/**
 * Class to connect to server
 */
public class Server {

    private RequestQueue queue;
    private String url;
    private String appID;

    public Server(Context context) {
        queue = Volley.newRequestQueue(context);
        url = "http://45.55.65.113/";
        appID = "JamesJordanPhotos";
    }

    /**
     * gets info (lat, long, url) for next stage in scavenger hunt
     * @param stage the stage of which to get the info
     * @param callback callback which is set later in order to do stuff with the info
     */
    public void getNextInfo(final int stage, final Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url+"scavengerhunt",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        double latitude = 0;
                        double longitude = 0;
                        String video = "";
                        boolean isLast = false;
                        try {
                            JSONArray path = response.getJSONArray("path");
                            if (path.length() == stage + 1) {
                                isLast = true;
                            }
                            JSONObject stageInfo = (JSONObject) path.get(stage);
                            latitude = stageInfo.getDouble("latitude");
                            longitude = stageInfo.getDouble("longitude");
                            video = stageInfo.getString("s3id");
                        } catch (Exception e) {
                            callback.callback(false, 0, 0, null, false);
                            Log.e("Error!", e.getMessage());
                        }
                        callback.callback(true, latitude, longitude, video, isLast);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage()+ " no internet...");
                        callback.callback(false, 0, 0, null, false);
                    }
                }
        );
        queue.add(request);
    }

    /**
     * UPDATE: Not needed because no need of
     * @param stage
     * @param callback
     */
    public void getUploadedImage(int stage, final GetImageCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url + "userdata/" + appID,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String key = "";
                        try {
                            JSONArray data = response.getJSONArray("data");

                        } catch (Exception e) {
                            callback.callback(false, null);
                            Log.e("ERROR!", e.getMessage());
                        }
                        callback.callback(true, key);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                        callback.callback(false, null);
                    }
                }
        );
        queue.add(request);
    }

    /**
     * Post image information to sever
     * @param imageKey string containing UUID of image
     * @param location stage (indexing from 1) of photo
     * @param callback to allow for implementation later
     */
    public void postImage(String imageKey, int location, final PutCallback callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("imageKey", imageKey);
            body.put("imageLocation", location);
        } catch (Exception e) {
            Log.e("JSONException", e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url + "userdata/" + appID,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String statusCode = "";
                        try {
                            statusCode = response.toString();
                        } catch (Exception e) {
                            callback.callbackPut(false, null);
                        }
                        callback.callbackPut(true, statusCode);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage() + " ERROR!");
                        callback.callbackPut(false, "error :(");
                    }
                }
        );
        queue.add(request);
    }
}