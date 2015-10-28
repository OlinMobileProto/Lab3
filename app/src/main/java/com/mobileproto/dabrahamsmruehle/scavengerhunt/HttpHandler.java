package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matt on 10/27/15.
 */
public class HttpHandler
{
    public RequestQueue queue;
    private static final String ipAddress = "45.55.65.113";
    private static final String objToRequest = "/scavengerhunt";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private Context parentContext;

    public HttpHandler(Context context)
    {
        queue = Volley.newRequestQueue(context);
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        parentContext = context;
        Log.d("SharedPrefsHttp", "handler created");
    }

    public void updatePathFromServer()
    {
        sharedPreferencesEditor.putLong("target_longitude", 0); // so that you can't immediately "clear" a step while waiting for the HTTP response.
        Log.d("SharedPrefsHttp", "updatePathFromServer called");
        JSONObject body = new JSONObject();
        String address = "http://" + ipAddress + objToRequest;
        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.GET,
                address,
                body,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Log.d("SharedPrefsHttp", "response received");
                        sendServerDataToSharedPreferences(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                        Log.d("SharedPrefsHttp", "error received");
                        Log.d("SharedPrefsHttp", error.getMessage());
                    }
                }
        );
        queue.add(getRequest);
        Log.d("SharedPrefsHttp", "get request added to queue");
    }

    public void sendServerDataToSharedPreferences(JSONObject input)
    {
        int currentObjective = sharedPreferences.getInt("current_step", 1);
        Log.d("SharedPrefsHttp", "callback sendServerData called");
        try
        {
            JSONArray jArray = input.getJSONArray("path");
            int size = jArray.length();
            if (currentObjective > size) {
                Toast.makeText(parentContext, "Congratulations! Hunt completed. Start a new hunt if you still want to play.", Toast.LENGTH_LONG);
            } else {
                for (int i = 0; i < size; i++) {
                    JSONObject currentJsonObject = jArray.getJSONObject(i);
                    try {
                        int currentId = currentJsonObject.getInt("id");
                        Log.d("SharedPrefsHttp", "id: " + String.valueOf(currentId) + ", currObjective: " + String.valueOf(currentObjective));
                        if (currentId == currentObjective) {
                            Log.d("SharedPrefsHttp", "determined equal");
                            double longitude = currentJsonObject.getDouble("longitude");
                            double latitude = currentJsonObject.getDouble("latitude");
                            String targetVid = currentJsonObject.getString("s3id");
                            Log.d("SharedPrefsHttp", "longitude: " + String.valueOf(longitude) + ", latitude: " + String.valueOf(latitude) + ", video: " + targetVid);
                            sharedPreferencesEditor.putLong("target_longitude", Double.doubleToLongBits(longitude));
                            sharedPreferencesEditor.putLong("target_latitude", Double.doubleToLongBits(latitude));
                            sharedPreferencesEditor.putString("target_video", targetVid);
                            sharedPreferencesEditor.commit();
                        }
                    } catch (JSONException ex) {
                        Log.e("Error", ex.getMessage());
                    }
                }
            }

        } catch (JSONException ex)
        {
            Log.e("Error", ex.getMessage());
        }
    }

}
