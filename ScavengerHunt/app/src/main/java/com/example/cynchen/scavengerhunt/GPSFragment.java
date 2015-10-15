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
public class GPSFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public GPSFragment() {
    }
    // Video Initializations:
    public VideoView clue;
    public ArrayList<String> cluesLink = new ArrayList<>();
    public int clueCounter;
    //GPS Initializations:
    GPSTracker gps;
    Timer timer;
    TimerTask myTimerTask;
    final Handler handler = new Handler();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 10 * 1; // 1 minute
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

        // Create the LocationRequest object
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
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3146.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3145.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3144.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3147.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3141.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp");
        clueCounter = 1;
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

        //MAP Code
        setUpMapIfNeeded();

        tvLocation = (TextView) rootView.findViewById(R.id.text_location);
        arrayPoints = new ArrayList<LatLng>();


        timer = new Timer();
        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        display_location();
                    }
                });
            }
        };
        //singleshot delay 1000 ms
        timer.scheduleAtFixedRate(myTimerTask, 0, 1000 * 10 * 1);


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
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        UiSettings mapSettings;
        mapSettings = googleMap.getUiSettings();

        mapSettings.setZoomControlsEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    private void handleNewLocation(Location myLocation) {
        double currentLatitude = myLocation.getLatitude();
        double currentLongitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (myLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(myLocation);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
    public void display_location(){
        gps = new GPSTracker(getActivity());

        //check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            Log.d("Place:", String.valueOf(latitude));
            tvLocation.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
            if (42.290 < latitude & latitude < 42.295 & longitude < -71.263 & longitude > -71.265) {
                Toast.makeText(getActivity(), "You have arrived!", Toast.LENGTH_LONG).show();
                CameraFragment camera_frag = new CameraFragment();
                ((MainActivity)getActivity()).transitionToFragment(camera_frag);
            }
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
