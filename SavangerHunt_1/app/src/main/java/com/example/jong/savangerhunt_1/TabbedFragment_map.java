package com.example.jong.savangerhunt_1;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by root on 10/22/15.
 */
public class TabbedFragment_map extends android.support.v4.app.Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks{
    public static final String ARG_SECTION_NUMBER = "2";
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Boolean mapReady = false;
    private boolean hasarrived = false;
    private GPSTracker gps;
    private StageData stageData;
    private Button camera;


    private TextView textview;
    public TabbedFragment_map() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.maplayout, container, false);
        create_button(v, "camera");
        stageData= new StageData(this.getActivity());
        textview = (TextView) v.findViewById(R.id.stage_text);
        textview.setText("    Stage # ".concat(String.valueOf(stageData.getVisiblestage())));
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        gps = new GPSTracker(getActivity(), this);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }


    public void create_button(View v, String button) {
        switch (button) {
            //TODO:button name change to small case
            case ("camera"): {
                camera = (Button) v.findViewById(R.id.Camera_button);
                camera.setEnabled(hasarrived);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transitionToFragment(new Photoview());

                    }
                });
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("ONCONNECTED","this was called");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        mapReady = true;
        googleMap = mMap;
        updateMapWithLocation();
    }
    private void updateMapWithLocation() {
        if (mapReady) {
            LatLng locLL = new LatLng(gps.getLatitude(), gps.getLongitude());
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(locLL)
                    .title("Your current location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(locLL));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        }
    }

    public void locationChanged() {
        // Called from GPSTracker when the location changes
        updateMapWithLocation();
        checkArrival();
    }

    private void checkArrival() {
        double currLat = gps.getLatitude();
        double currLong = gps.getLongitude();
        String[] destLoc = stageData.getStageLocation(stageData.getCurrstage());
        double destLat = Double.parseDouble(destLoc[0]);
        double destLong = Double.parseDouble(destLoc[1]);
        if (Math.abs(destLat - currLat) < 0.001 && Math.abs(destLong - currLong) < 0.001) {
            // We are close enough to our destination to count it as arrived
            hasarrived=true;
            camera.setEnabled(hasarrived);
        }
        else{
            hasarrived=false;
            camera.setEnabled(hasarrived);
        }
    }


    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_frame, fragment);
        transaction.commit();
    }

    public void updatemapview(int stage){
        textview.setText("    Stage # ".concat(String.valueOf(stage)));
    }

}
