package hieunguyen.com.scavengerhunt.Fragments;

import android.app.Activity;
import android.app.Fragment;
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

    private class mapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("I'm here"));
            dbService = new DbService(getActivity().getBaseContext());

            // Tries to get active clue. If it doesn't exist, get clue based on clueNumber (1)
            currClue = dbService.getClue(0);
            if (currClue == null) {
                Log.d(TAG, "currClue is null");
                currClue = dbService.getClue(clueNumber);
                dbService.changeActiveClue(-1, clueNumber);
            }

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
            double locTol = 0.00002;
            Log.d(TAG, location.toString());

            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();


            if (currentLatitude > currClue.getLatitude() - locTol
                    && currentLatitude < currClue.getLatitude() + locTol
                    && currentLongitude > currClue.getLongitude() - locTol
                    && currentLongitude < currClue.getLongitude() + locTol) {
                Log.d(TAG, "IN RANGE");

                // Once clue has been reached, change the active clue, increment clueNumber,
                // and get the new clue
                dbService.changeActiveClue(clueNumber, clueNumber++);
                clueNumber++;
                currClue = dbService.getClue(clueNumber);
            }

            LatLng latLng = new LatLng(currentLatitude, currentLongitude);

            mOptions.position(latLng);

            if (mMarker != null) {
                Log.d(TAG, "Removing marker");
                mMarker.remove();
            }
            mMarker = mMap.addMarker(mOptions);

            pOptions.add(latLng);
            mPolyline = mMap.addPolyline(pOptions);

            if (firstLocation) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 18.0));
                firstLocation = false;
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
}
