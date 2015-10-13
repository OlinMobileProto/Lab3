package com.example.lwilcox.thehunt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class VideoFragment extends Fragment {
    private View myFragmentView;
    public ImageView img1, img2, img3, img4, img5, img6;
    public ArrayList<ImageView> images = new ArrayList<ImageView>();
    public Integer imageIndex = 0;
    //public CameraManager myCamera =  new CameraManager(getActivity().getBaseContext()); //issues are here
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;

    public boolean locationFound = false;
    public int[] clue_location; //[latitude, longitude]
    public int current_clue;
    private int time_interval = 100; //milliseconds
    private int min_distance_for_updates = 1; //meters
    public LocationManager locationManager;
    public double[] position = new double[2];
    public final int LOCATION_REQUEST = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    public Uri u;
    public Drawable fullImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_video, container, false);

        img1 = (ImageView) myFragmentView.findViewById(R.id.imageView1);
        img2 = (ImageView) myFragmentView.findViewById(R.id.imageView2);
        img3 = (ImageView) myFragmentView.findViewById(R.id.imageView3);
        img4 = (ImageView) myFragmentView.findViewById(R.id.imageView4);
        img5 = (ImageView) myFragmentView.findViewById(R.id.imageView5);
        img6 = (ImageView) myFragmentView.findViewById(R.id.imageView6);

        images.add(img1);
        images.add(img2);
        images.add(img3);
        images.add(img4);
        images.add(img5);
        images.add(img6);
        final Drawable myDrawable = getActivity().getResources().getDrawable(R.drawable.cameraimage);
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setImageDrawable(myDrawable);
        }
        //TODO: add VideoView to VideoFragment

        setCameraButton(u);

        ////////////////////////////////////////////GPS Functionality
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Request location services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //if you have a high enough version of android, request permissions
            Log.d("Permission request","Doing permission location request");
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(getActivity().getBaseContext(), "This app won't work if you don't let it find your location.",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_REQUEST);
                }
            }
        } else{
            Log.d("Permission request","no location permission request - you have old android");
            Toast.makeText(getActivity().getBaseContext(), "You probably want a higher version of android",
                    Toast.LENGTH_SHORT).show();
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                time_interval,
                min_distance_for_updates,
                new MyLocationListener()
        );

        return myFragmentView;
    }


    public void downloadClue(){
        //TODO: integrate downloadClue with Camera stuff
        AmazonS3 s3 = new AmazonS3(getActivity().getBaseContext());

        String file_name; //this is given to us
        s3.download(current_clue);
        //do stuff with clue_location
        current_clue += 1;
    }

    public void uploadPicture(){
        //TODO: integrate uploadPicture with Camera stuff
        AmazonS3 s3 = new AmazonS3(getActivity().getBaseContext());

        //do camera stuff
        String file_name = "";
        String clue_info = "information on clue " + current_clue;
        s3.upload(file_name, current_clue, clue_info);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getBaseContext(), "Thanks for the permission <3",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Really - this app won't work if you don't let it find your location.",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            position[0] = location.getLongitude();
            position[1] = location.getLatitude();
            Log.d("Location", position[0] + ", " + position[1]);
            checkLocation();
        }

        public void checkLocation(){
            //use position[0], position[1]
            //TODO: write function that checks to see if you are within 10M of clue location
            locationFound = false; //true if you are within distance
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getActivity().getBaseContext(), "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String s) {
            Toast.makeText(getActivity().getBaseContext(), "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String s){
            Toast.makeText(getActivity().getBaseContext(), "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void setCameraButton(Uri u){
        //final Uri uri = u;

        for (int i = 0; i < images.size(); i++) {
            if (imageIndex == i) {
                images.get(imageIndex).setClickable(true);
            }
            else {
//                images.get(i).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            String filePath = uri.getPath();
//                            InputStream inputStream  = new FileInputStream(filePath);
//                            //InputStream inputStream = getActivity().getBaseContext().getContentResolver().openInputStream(uri);
//                            fullImage = Drawable.createFromStream(inputStream, uri.toString() );
//                        } catch (FileNotFoundException e) {
//
//                        }
//                        final Dialog nagDialog = new Dialog(getActivity().getBaseContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        nagDialog.setCancelable(false);
//                        nagDialog.setContentView(R.layout.preview_image);
//                        Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
//                        ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
//                        ivPreview.setBackgroundDrawable(fullImage);
//
//                        btnClose.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View arg0) {
//
//                                nagDialog.dismiss();
//                            }
//                        });
//                        nagDialog.show();
//                    }
//                });
                images.get(imageIndex).setClickable(false);
            }
        }


        images.get(imageIndex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            images.get(imageIndex).setImageBitmap(imageBitmap);
            Uri u = data.getData();
            imageIndex ++;
        }
        setCameraButton(u);
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
