package hieunguyen.com.scavengerhunt.Activities;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

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
        mMap.clear();
        mLocationProvider.disconnect();
    }

    public Uri getVideoUrl(int clueNumber){
        // TODO: Implement getting the actual URL here
        return Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.MOV");
    }

    @Override
    public void onVideoDone() {
        Log.d(TAG, "DONE");
        mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
    }

    @Override
    public void handleNewLocation(Location location) {
        if (mapFragment != null) {
            mapFragment.updateUI(location);
        }

    }
}
