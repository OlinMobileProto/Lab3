package com.example.cynchen.scavengerhunt.GPS;

import android.content.IntentSender;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.cynchen.scavengerhunt.Camera.CameraFragment;
import com.example.cynchen.scavengerhunt.MainActivity;
import com.example.cynchen.scavengerhunt.R;
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

import java.util.ArrayList;

public class GPSFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Shows the video of clue again, and then shows GPS location that constantly
    //alerts user of when their location changed.

    public GPSFragment() {
    }

    //Sets the clue title and number:
    public TextView clueTitle;

    // Video Initializations:
    public VideoView clue;
    //List of links for the videos, list of longitudes, list of latitudes
    public ArrayList<String> cluesLink = new ArrayList<>();
    public ArrayList<String> longitude_list = new ArrayList<>();
    public ArrayList<String> latitude_list = new ArrayList<>();

    //clueCounter is related to the clue that the fragment is on in the cycle
    public int clueCounter;

    //GPS Initializations:
    GPSTracker gps;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 5 * 1; //10 seconds
    private static final long FASTEST_INTERVAL = 1000 * 5 * 1; // 10 seconds
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Builds the API client
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

    //ONCREATE: Begin creating the view and layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gps, container, false);

        //Calls increment_counter during initialization to get the number of clue that it is on after finishing the first clue
        clueCounter = ((MainActivity)getActivity()).return_counter();
        Log.d("Counter", String.valueOf(clueCounter));

        //Setting the clue title to be aligned with what clue the user is on
        clueTitle = (TextView) rootView.findViewById(R.id.clueTitle);
        clueTitle.setText("Clue " + Integer.toString(clueCounter));


        //VIDEO Code:
        //Links of videos retrieved from main activity's volley request
        cluesLink = ((MainActivity)getActivity()).locations_videos;
        clue = (VideoView) rootView.findViewById(R.id.clue);

        //Same video code as videofragment that opens the media controller with the video associated with the clue number
        //This shows up on the first half of the fragment
        clue = (VideoView) rootView.findViewById(R.id.clue);
        clue.setVideoURI(Uri.parse(cluesLink.get(clueCounter - 1)));
        clue.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                clue.seekTo(1);
                MediaController mc = new MediaController(getContext());
                clue.setMediaController(mc);
                mc.setAnchorView(clue);
            }
        });


        //MAP Code: Setting up map, and updating map location every 5 seconds.
        //Map setup at the very beginning:
        setUpMapIfNeeded();

        return rootView;
    }

    //Called oncreateview to set up the map first:
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
        //change UI settings of map:
        UiSettings mapSettings;
        mapSettings = googleMap.getUiSettings();

        //Zoom enabled:
        mapSettings.setZoomControlsEnabled(true);

        //Map Type: Hybrid - mixture of normal and satellite views
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    //When LocationListener is onConnected:
    @Override
    public void onConnected(Bundle bundle) {
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if (myLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            //calls handleNewLocation when connected to addMarker once the cell phone is reopened
            handleNewLocation(myLocation);
        }
    }

    private void handleNewLocation(Location myLocation) {
        //get the longitude and latitude to put a new marker
        double currentLatitude = myLocation.getLatitude();
        double currentLongitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //Puts marker with following options on map with an 18 zoom
        MarkerOptions options = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    //Reset up map if needed on resume and reconnect mGoogleApiClient
    @Override
    public void onResume() {
        super.onResume();
        //Re-setup map if needed
        setUpMapIfNeeded();

        //Connect to API client
        mGoogleApiClient.connect();
    }

    //DisconnectmGoogleApiClient onPause
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            //Disconnect if onPause
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    //When user leaves fragment and when phone goes to sleep: mGoogleApiClient disconnects
    @Override
    public void onStop() {
        super.onStop();
        //Disconnect and stop timer if fragment is exited from
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    //If LocationListener fails when connecting
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

    //When user walks to new locations, display_location happens with new longitude and latitude
    @Override
    public void onLocationChanged(Location location) {
        Log.d("location changed", "HI");
        Location myLocation = location;

        //gets the right longitude and latitude that the clue should be in:
        longitude_list =  ((MainActivity) getActivity()).longitudes_list;
        latitude_list =  ((MainActivity) getActivity()).latitudes_list;

        //display_location runs with new location and marker gets put on the map too
        display_location(Double.parseDouble(longitude_list.get(clueCounter - 1)), Double.parseDouble(latitude_list.get(clueCounter - 1)),myLocation);
        handleNewLocation(myLocation);
    }

    //OnLocationChanged display_location function, which finds whether or not the location is the right
    //location and then starts the camera fragment if the person is in the right location
    public void display_location(double longitude_real, double latitude_real, Location myLocation){

        gps = new GPSTracker(getActivity());

        //check if GPS enabled
        if (gps.canGetLocation()) {

            //get the longitude and latitude from onLocationChanged to update map and check it with the real ones:
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();

            Log.d("Latitude",Double.toString(latitude_real));
            Log.d("Longitude",Double.toString(longitude_real));

            //Show the new location as a toast:
            Toast.makeText(getActivity(),Double.toString(latitude) + " " + Double.toString(longitude), Toast.LENGTH_LONG).show();

            //checks if longitude and latitude is in the right range
            if ((latitude_real-0.00029) < latitude & latitude < (Math.abs(latitude_real)+0.00029) & Math.abs(longitude) < (Math.abs(longitude_real)+0.00029) & Math.abs(longitude) > (Math.abs(longitude_real)-0.00029)) {
                //If it is in the right range, it will show the congratulations alert and then change to the camera fragment
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

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
