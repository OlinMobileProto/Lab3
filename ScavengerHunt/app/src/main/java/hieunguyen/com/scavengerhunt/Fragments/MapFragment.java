package hieunguyen.com.scavengerhunt.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hieunguyen.com.scavengerhunt.Activities.HuntActivity;
import hieunguyen.com.scavengerhunt.Activities.PhotoActivity;
import hieunguyen.com.scavengerhunt.Data.ClueDAO;
import hieunguyen.com.scavengerhunt.Data.DbService;
import hieunguyen.com.scavengerhunt.Data.HttpHandler;
import hieunguyen.com.scavengerhunt.Interfaces.DestinationCallback;
import hieunguyen.com.scavengerhunt.R;


public class MapFragment extends Fragment {

    public static final String TAG = "GPS_FRAGMENT";
    @Bind(R.id.map) MapView mMapView;
    public GoogleMap mMap;
    public Marker mMarker;
    private MarkerOptions mOptions = new MarkerOptions().title("I am here");
    private Polyline mPolyline;
    private PolylineOptions pOptions = new PolylineOptions().width(5).color(Color.RED);

    private boolean firstLocation = true;
    private int clueNumber = 1;
    private ClueDAO currClue;
    private DbService dbService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new mapReadyCallback());

        return rootView;
    }

    // Async method for when map is ready
    private class mapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            // Initiates variables. Location temporarily set to off the coast of Africa.
            mMap = googleMap;
            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("I'm here"));
            dbService = new DbService(getActivity().getBaseContext());

            // Tries to get currently active clue. If it doesn't exist, get clue based on clueNumber (1)
            currClue = dbService.getClue(0);
            if (currClue == null) {
                currClue = dbService.getClue(clueNumber);
                dbService.changeActiveClue(-1, clueNumber);
            }

            // Initialize map view
            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateUI(Location location) {

        if (mMap != null) {
            double locTol = 1; //0.00002; // Roughly 3 meters (or 10 feet)
            Log.d(TAG, location.toString());

            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            // If location within range
            if (currentLatitude > currClue.getLatitude() - locTol
                    && currentLatitude < currClue.getLatitude() + locTol
                    && currentLongitude > currClue.getLongitude() - locTol
                    && currentLongitude < currClue.getLongitude() + locTol) {
                Log.d(TAG, "IN RANGE");

                // Once clue has been reached, launch PhotoActivity
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                startActivity(intent);
            }

            LatLng latLng = new LatLng(currentLatitude, currentLongitude);

            // Updates markerOptions and marker
            mOptions.position(latLng);
            if (mMarker != null) {
                Log.d(TAG, "Removing marker");
                mMarker.remove();
            }
            mMarker = mMap.addMarker(mOptions);

            // Adds current location to the polyline and plot it
            pOptions.add(latLng);
            mPolyline = mMap.addPolyline(pOptions);

            // If it is the first time the map is being viewed, zoom in. Else, maintain zoom state
            if (firstLocation) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 18.0));
                firstLocation = false;
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }


}
