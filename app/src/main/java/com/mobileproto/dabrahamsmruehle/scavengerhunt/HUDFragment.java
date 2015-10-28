package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HUDFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HUDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HUDFragment extends Fragment implements OnMapReadyCallback
{
    private static final String ERROR_TAG = "HUD Fragment Error";

    private OnFragmentInteractionListener mListener;
    public LocationManager locationManager;
    public LocationListener locationListener;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;
    public boolean isAtTarget;
    public HttpHandler httpHandler;
    @Bind(R.id.curr_clue_button) Button playCurrentClue;
    @Bind(R.id.mapview) MapView mapView;
    GoogleMap map;

    @Bind(R.id.take_photo_btn) Button takePhotoBtn;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HUDFragment.
     */
    public static HUDFragment newInstance()
    {
        HUDFragment fragment = new HUDFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HUDFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        httpHandler = new HttpHandler(getActivity());
        httpHandler.updatePathFromServer();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPrefsEditor = sharedPrefs.edit();
        View view = inflater.inflate(R.layout.fragment_hud, container, false);
        ButterKnife.bind(this, view);
        playCurrentClue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((HUDFragment.OnFragmentInteractionListener) getActivity())
                        .onFragmentInteraction(Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp")); // can be replaced with a string button or fragment now; video ID is no longer communicated via the onFragmentInteraction URI and instead uses the sharedPreferences values.

            }
        });
        takePhotoBtn.setEnabled(false);
//        takePhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //takePhoto();
//                int currentStep = sharedPrefs.getInt("current_step", 1);
//                int nextStep = currentStep + 1;
//                sharedPrefsEditor.putInt("current_step", nextStep); // sets the current step to be the one we want the location/video data for.
//                httpHandler.updatePathFromServer(); // has the handler query the server for the latitude/longitude/videoId combos; handler then sets the sharedPreferences for these based on the "current_step" sharedpreference.
//                takePhotoButton.setEnabled(false); //re-greys it out until the next time we get location info, at least.
//            }
//        });
        Log.d("GpsVals", "got to here: HUD Fragment, about to make it handle GPS");
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location)
            {
                Log.d("GpsVals", "onLocationChanged: " + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                checkIfClose(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener); // Do we really need to check for these permissions if we're putting them in the manifest xml file?
        } catch (SecurityException e) {
            Log.e(ERROR_TAG, "GPS Permissions were not given. :(");
        }
        Log.d("GpsVals", "got to here: HUD Fragment, just requested location updates.");

        takePhotoBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((HUDFragment.OnFragmentInteractionListener) getActivity())
                        .onFragmentInteraction("take_photo_button");
            }
        });

        int checkGooglePlayServices =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(
                    checkGooglePlayServices, getActivity(), 1122).show();
        }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        return view;
    }

    public void checkIfClose(Location location)
    {
        Location destination = new Location("SERVER");
        double currentLongitude = Double.longBitsToDouble(sharedPrefs.getLong("target_longitude", 0));
        double currentLatitude = Double.longBitsToDouble(sharedPrefs.getLong("target_latitude", 0));

        float distanceThreshold = sharedPrefs.getFloat("distance_threshold", 500); // 100 meters is the default. Not sure if this is reasonable.
        //Ideally, distance_threshold will be set in the Settings options/tabs/etc. Right now, it's 500 because it's cold outside.
        destination.setLongitude(currentLongitude);
        destination.setLatitude(currentLatitude);
        float distanceToTarget = location.distanceTo(destination);
        isAtTarget = (distanceToTarget < distanceThreshold);
        takePhotoBtn.setEnabled(isAtTarget);
        Log.d("GpsVals", "current distance: " + String.valueOf(distanceToTarget));
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy()
    {

        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(String s);
        public void onFragmentInteraction(Uri u);
    }

}
