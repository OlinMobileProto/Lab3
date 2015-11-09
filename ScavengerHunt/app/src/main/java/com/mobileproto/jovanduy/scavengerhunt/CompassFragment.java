package com.mobileproto.jovanduy.scavengerhunt;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class CompassFragment extends Fragment implements SensorEventListener{

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;
    private String longitude;
    private String latitude;
    private String targetLongitude;
    private String targetLatitude;
    private double distance = 0;
    private String direction = "";
    private TextView tvHeading;
    private String TAG = "CompassFragment";
    private TextView directionText;
    private TextView distanceText;

    public CompassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_compass, container, false);
        createImageview(rootView);


        Bundle bundle = this.getArguments();
        longitude = bundle.getString("longitude", null);
        latitude = bundle.getString("longitude", null);
        targetLongitude = bundle.getString("target_longitude", null);
        targetLatitude = bundle.getString("target_latitude", null);
        CalculateLocation calculateLocation = new CalculateLocation();
        distance = calculateLocation.calculateDistance(latitude, longitude, targetLatitude, targetLongitude);
        direction = calculateLocation.calculateDirection();
        Log.d("direction", "!!!"+direction+"@@@@");
        Log.d("distance", "#####"+String.valueOf(distance)+"$$$$$");
        directionText.setText("Direction:" + "\n" + direction);
        distanceText.setText("Distance:" + "\n"+ String.valueOf(distance)+"\n"+"meters");
        createBackVideoButton(rootView);
        return rootView;
    }
    public void createBackVideoButton(View v){
        Button button = (Button) v.findViewById(R.id.Back_Video_Button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                VideoFragment videoFragment = new VideoFragment();
                transitionToFragment(videoFragment);
            }}
        );
    }
    public void createImageview(View v){
        image = (ImageView) v.findViewById(R.id.Compass_View);
        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView)v.findViewById(R.id.Heading);
        directionText = (TextView) v.findViewById(R.id.directionText);
        distanceText = (TextView) v.findViewById(R.id.distanceText);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();

        // to stop the listener and save battery
//        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");


        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}

