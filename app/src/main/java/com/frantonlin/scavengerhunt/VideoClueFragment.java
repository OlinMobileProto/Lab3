package com.frantonlin.scavengerhunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by keenan on 10/13/15.
 */

public class VideoClueFragment extends Fragment {

//    String testURL = "https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp";
    public static final String TAG = VideoClueFragment.class.getSimpleName();
    public LocationManager locationManager;
    public LocationListener locationListener;
    private double target_threshold = 0.0001; // Have to be within about 11 m of target
    Location currentLoc;
    private double latitude;
    private double longitude;

    public VideoClueFragment(){

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.clue, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        final Button checkLocationButton = (Button) view.findViewById(R.id.checkPos);

        // When user clicks 'CHECK' button, check if they are within range by locating their GPS
        checkLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, "GPS: Latitude: " + String.valueOf(location.getLatitude()) + ", Longitude: " + String.valueOf(location.getLongitude()));
                    }
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                    public void onProviderEnabled(String provider) {

                    }
                    public void onProviderDisabled(String provider) {

                    }
                };
                try {
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener); USE IF WANT LIVE UPDATING LOCATION STATUS
                    currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    checkLocation(currentLoc);
                    Log.d(TAG, "GPS: Latitude: " + String.valueOf(currentLoc.getLatitude()) + ", Longitude: " + String.valueOf(currentLoc.getLongitude()));
                } catch (SecurityException ex) {
                    Log.e("ERROR", ex.getMessage());
                }
            }
        });

        //Pares the video URL and play in the view, also include a media controller
        getClueInfoWithCallback();
    }

    public void getClueInfoWithCallback() {
        ((MainActivity) getActivity()).getHttpHandler().getInfo(new InfoCallback() {
            @Override
            public void callback(boolean success, HashMap<String, String> clueInfo) {
                if (success) {
                    Log.d("Success", Boolean.toString(success));
                    loadVideo("https://s3.amazonaws.com/olin-mobile-proto/" + clueInfo.get("s3id"));
                    TextView clueLabel = (TextView) getActivity().findViewById(R.id.clueLabel);
                    clueLabel.setText("Clue " + clueInfo.get("id") + "/" + clueInfo.get("numClues"));
                    latitude = Double.parseDouble(clueInfo.get("latitude"));
                    longitude = Double.parseDouble(clueInfo.get("longitude"));
                } else {
                    Log.d("Failure", Boolean.toString(success));
                }
            }
        }, ((MainActivity) getActivity()).getClueNum());
    }

    public void loadVideo(String videoUrl) {
        Uri uri=Uri.parse(videoUrl);
        VideoView videoView = (VideoView) getActivity().findViewById(R.id.videoView);
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    public void checkLocation(Location loc)
    {
        // Check for distance to the target given a specific latitude and longitude threshold of being within range
        if (Math.abs(loc.getLatitude() -  latitude) < target_threshold && (Math.abs(loc.getLongitude() - longitude) < target_threshold))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set title
            alertDialogBuilder.setTitle("CONGRATULATIONS!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Continue to next clue?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((MainActivity) getActivity()).dispatchTakePictureIntent();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set title
            alertDialogBuilder.setTitle("NOT QUITE CLOSE ENOUGH!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Try Again!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CONTINUE WITH THE SAME VIEW
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

}
