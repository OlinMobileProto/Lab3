package hieunguyen.com.scavengerhunt.Activities;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import hieunguyen.com.scavengerhunt.Data.LocationProvider;
import hieunguyen.com.scavengerhunt.Fragments.ClueFragment;
import hieunguyen.com.scavengerhunt.Fragments.MapFragment;
import hieunguyen.com.scavengerhunt.R;

public class HuntActivity extends AppCompatActivity implements ClueFragment.onVideoDoneListener,
    LocationProvider.LocationCallback {

    public static final String TAG = "HUNT_ACTIVITY";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationProvider mLocationProvider;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ClueFragment())
                    .commit();
        }

        mLocationProvider = new LocationProvider(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMap != null) {
            mMap.clear();
        }
        mLocationProvider.disconnect();
    }

    // Generates URL to get video of current clue
    public Uri getVideoUrl(int clueNumber){
        return Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.MOV");
    }

    @Override
    // onVideoDoneListener from ClueFragment. When video completes clue fragment transitions
    // into map fragment
    public void onVideoDone() {
        Log.d(TAG, "DONE");
        mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
    }

    @Override
    // Implementing the LocationProvider.LocationCallback interface's required method.
    public void handleNewLocation(Location location) {
        if (mapFragment != null) {
            mapFragment.updateUI(location);
        }
    }
}
