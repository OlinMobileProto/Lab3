package hieunguyen.com.scavengerhunt.Data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hieunguyen.com.scavengerhunt.Interfaces.DestinationCallback;
import hieunguyen.com.scavengerhunt.Interfaces.PhotoPostCallback;
import hieunguyen.com.scavengerhunt.R;

/**
 * Created by hieunguyen on 10/17/15.
 */
public class HttpHandler {
    public static final String APP_ID = "patandhieu";

    public RequestQueue queue;
    public Context context;

    public HttpHandler(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void getDestinations(final DestinationCallback callback) {
        String url = context.getString(R.string.ip_address);
        url += "/scavengerHunt";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        JSONArray locations = null;
                        try {
                            locations = response.getJSONArray("path");
                        } catch (JSONException e) {
                            Log.d("HTTP", "JSON ERROR");
                        }
                        callback.destCallback(true, locations);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "ERROR");
                    }
                }
        );

        queue.add(request);
    }

    public void postPhotoData(String imageKey, String imageLocation, final PhotoPostCallback callback) {
        String url = context.getString(R.string.ip_address);
        String route = "/userdata/" + APP_ID;
        url += route;

        JSONObject body = new JSONObject();
        try {
            body.put("imageKey", imageKey);
            body.put("imageLocation", imageLocation);
        } catch (Exception e) {
            Log.e("JSONException", e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                    }
                }
        );
    }


}
