package hieunguyen.com.scavengerhunt.Activities;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import hieunguyen.com.scavengerhunt.Data.ClueDAO;
import hieunguyen.com.scavengerhunt.Data.DbService;
import hieunguyen.com.scavengerhunt.Data.LocationProvider;
import hieunguyen.com.scavengerhunt.Fragments.ClueFragment;
import hieunguyen.com.scavengerhunt.Fragments.MapFragment;
import hieunguyen.com.scavengerhunt.R;

public class HuntActivity extends AppCompatActivity implements ClueFragment.onVideoDoneListener,
    LocationProvider.LocationCallback {

    private static final String TAG = HuntActivity.class.getName();

    private static final String BASE_S3_URL = "https://s3.amazonaws.com/olin-mobile-proto/";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationProvider mLocationProvider;
    private MapFragment mapFragment;
    private DbService mDbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        mDbService = new DbService(this);

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

    public Uri getVideoUrl(){
        ClueDAO currentClue = mDbService.getClue(0);
        String s3id = currentClue.getS3id();
        return Uri.parse(BASE_S3_URL + s3id);
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
