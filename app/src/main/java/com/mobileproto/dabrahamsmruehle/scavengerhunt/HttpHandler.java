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
    }

    public void updatePathFromServer()
    {
        sharedPreferencesEditor.putLong("target_longitude", 0); // so that you can't immediately "clear" a step while waiting for the HTTP response.
        JSONObject body = new JSONObject();
        String address = ipAddress + objToRequest;
        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.GET,
                address,
                body,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        sendServerDataToSharedPreferences(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                    }
                }
        );
        queue.add(getRequest);
    }

    public void sendServerDataToSharedPreferences(JSONObject input)
    {
        int currentObjective = sharedPreferences.getInt("current_step", 1);
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
                        if (currentId == currentObjective) {
                            sharedPreferencesEditor.putLong("target_longitude", currentJsonObject.getLong("longitude"));
                            sharedPreferencesEditor.putLong("target_latitude", currentJsonObject.getLong("latitude"));
                            sharedPreferencesEditor.putString("target_video", currentJsonObject.getString("s3id"));
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
