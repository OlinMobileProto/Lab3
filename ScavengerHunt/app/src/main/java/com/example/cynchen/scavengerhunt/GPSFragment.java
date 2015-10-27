package com.example.cynchen.scavengerhunt;

import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class GPSFragment extends VideoFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    //Extend VideoFragment, because of reuse of code

    public GPSFragment() {
    }
    // Video Initializations:
    public VideoView clue;
    public ArrayList<String> cluesLink = new ArrayList<>();
    public ArrayList<String> longitude_list = new ArrayList<>();
    public ArrayList<String> latitude_list = new ArrayList<>();
    public TextView clueTitle;
    public int clueCounter;

    //GPS Initializations:
    GPSTracker gps;
    Timer timer;
    TimerTask myTimerTask;
    final Handler handler = new Handler();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 5 * 1; //10 seconds
    private static final long FASTEST_INTERVAL = 1000 * 5 * 1; // 10 seconds
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;
    private ArrayList<LatLng> arrayPoints = null;
    PolylineOptions polylineOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object with the intervals that we set
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //ONCREATE: Begin
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_g, container, false);

        //VIDEO Code:
        cluesLink = ((MainActivity)getActivity()).locations_videos;

        //Calls increment_counter during initialization to get the number of clue that it is on after finishing the first clue
        clueCounter = ((MainActivity)getActivity()).return_counter();
        Log.d("Counter", String.valueOf(clueCounter));

        clue.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                GPSFragment gps_frag = new GPSFragment();
                ((MainActivity) getActivity()).transitionToFragment(gps_frag);
            }
        });


        //MAP Code: Setting up map, and updating map location and polyline every 10 seconds.
        //Map setup at the very beginning
        setUpMapIfNeeded();

        longitude_list =  ((MainActivity) getActivity()).longitudes_list;
        latitude_list =  ((MainActivity) getActivity()).latitudes_list;

        tvLocation = (TextView) rootView.findViewById(R.id.text_location);
        arrayPoints = new ArrayList<LatLng>();


        //Timertask calls display_location every 10 seconds
        timer = new Timer();
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        display_location(Double.parseDouble(longitude_list.get(clueCounter - 1)), Double.parseDouble(latitude_list.get(clueCounter - 1)));
                    }
                });
            }
        };
        //singleshot delay 1000 ms
        timer.scheduleAtFixedRate(myTimerTask, 0, FASTEST_INTERVAL);


        return rootView;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                //call setUpMap
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        UiSettings mapSettings;
        mapSettings = googleMap.getUiSettings();

        //Zoom enabled
        mapSettings.setZoomControlsEnabled(true);

        //Map Type: Hybrid - mixture of normal and satellite views
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (myLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            //calls handleNewLocation when connected
            handleNewLocation(myLocation);
        };
    }

    private void handleNewLocation(Location myLocation) {
        double currentLatitude = myLocation.getLatitude();
        double currentLongitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //Puts marker with following options on map with an 18 zoom
        MarkerOptions options = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    @Override
    public void onResume() {
        super.onResume();
        //Re-setup map if needed
        setUpMapIfNeeded();

        //Connect to API client
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            //Disconnect if onPause
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //Disconnect and stop timer if fragment is exited from
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        timer.cancel();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    //Unused on-functions
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    //Timertask calls display_location function, which finds whether or not the location is the right
    //location and then starts the camera fragment if the person is in the right location
    public void display_location(double longitude_real, double latitude_real){
        gps = new GPSTracker(getActivity());

        //check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            Log.d("Latitude",Double.toString(latitude_real));
            Log.d("Longitude",Double.toString(longitude_real));


            //tvlocation text shows current location
            tvLocation.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);

            //checks if longitude and latitude is in the right range
            if ((latitude_real-0.00029) < latitude & latitude < (Math.abs(latitude_real)+0.00029) & Math.abs(longitude) < (Math.abs(longitude_real)+0.00029) & Math.abs(longitude) > (Math.abs(longitude_real)-0.00029)) {
                //If it is in the right range, it will show the congratulations alert and then change to the camera fragment
                //WE NEED TO MAKE A LONGER DELAY BETWEEN THE TWO
                Toast.makeText(getActivity(), "You have arrived!", Toast.LENGTH_LONG).show();

                //3 seconds before the camera fragment opens
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CameraFragment camera_frag = new CameraFragment();
                        ((MainActivity) getActivity()).transitionToFragment(camera_frag);
                    }
                }, 3000);

            }

            //if not, a polyline between their current and previous location (10 seconds ago) is drawn
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);

            arrayPoints.add(latLng);
            polylineOptions.addAll(arrayPoints);
            googleMap.addPolyline(polylineOptions);

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
