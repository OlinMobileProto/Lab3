package com.example.lwilcox.thehunt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Context;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.widget.VideoView;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Video Fragment: Fragment that contains all video functionality. Displays the video and camera buttons
 */

public class VideoFragment extends Fragment {
    private View myFragmentView;
    public RelativeLayout relativeLayout;
    public ImageView img1, img2, img3, img4, img5, img6;
    public ArrayList<ImageView> images = new ArrayList<>();
    public ImageView finale;
    public Drawable blackCamera;
    public Drawable purpleCamera;

    public AmazonS3 s3;
    public Integer imageIndex = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public VideoView video;
    public MediaController mediaController;
    public String videoName;
    public int currentClue = 1;
    public Uri uri;

    public MyLocationListener locationListener;
    private int timeInterval = 100; //milliseconds
    private int minDistanceForUpdates = 1; //meters
    public LocationManager locationManager;
    public final int LOCATION_REQUEST = 1;

    public ArrayList<Uri> photoUriList = new ArrayList<Uri>();
    public Drawable fullImage;

    public ArrayList<Integer> allClueIds = new ArrayList<>();
    public ArrayList<Integer> allClueLats = new ArrayList<>();
    public ArrayList<Integer> allClueLongs = new ArrayList<>();
    public ArrayList<String> allClueS3ids = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_video, container, false);

        //add images
        img1 = (ImageView) myFragmentView.findViewById(R.id.imageView1);
        img2 = (ImageView) myFragmentView.findViewById(R.id.imageView2);
        img3 = (ImageView) myFragmentView.findViewById(R.id.imageView3);
        img4 = (ImageView) myFragmentView.findViewById(R.id.imageView4);
        img5 = (ImageView) myFragmentView.findViewById(R.id.imageView5);
        img6 = (ImageView) myFragmentView.findViewById(R.id.imageView6);
        relativeLayout = (RelativeLayout) myFragmentView.findViewById(R.id.searchView);
        images.add(img1);
        images.add(img2);
        images.add(img3);
        images.add(img4);
        images.add(img5);
        images.add(img6);
        blackCamera = getActivity().getResources().getDrawable(R.drawable.camera_black);
        purpleCamera = getActivity().getResources().getDrawable(R.drawable.camera_purple);
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setImageDrawable(blackCamera); //black camera button means it's not clickable
        }
        finale = (ImageView) myFragmentView.findViewById(R.id.finished); //for when you're done ;-)

        //get first clue
        s3 = new AmazonS3(getActivity().getBaseContext());
        locationListener = new MyLocationListener(getActivity().getBaseContext(), this);

        //set up video view
        video = (VideoView) myFragmentView.findViewById(R.id.videoView);
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        downloadDatabase();

        // set up camera buttons
        setImageDialog();

        //set up GPS Functionality
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //request location services
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
                timeInterval,
                minDistanceForUpdates,
                locationListener
        );

        return myFragmentView;
    }

    //downloads video names fom the database
    public void downloadDatabase(){
        HttpHandler handler = new HttpHandler(getActivity().getApplicationContext());
        handler.getClues(new Callback() {
            @Override
            public void callback(ArrayList<Integer> clueIds, ArrayList<Integer> clueLats, ArrayList<Integer> clueLongs, ArrayList<String> clueS3ids) {
                allClueIds = clueIds;
                allClueLats = clueLats;
                allClueLongs = clueLongs;
                allClueS3ids = clueS3ids;
                downloadClue();
            }
        });
    }

    //gets the next video and displays it, also changes the current camera button
    public void downloadClue(){
        //make sure video isn't playable
        if(currentClue != 1){
            video.stopPlayback();
        }

        //get clue
        if (currentClue < 7) {
            videoName = allClueS3ids.get(currentClue - 1);
            locationListener.setCluePosition(allClueLats.get(currentClue - 1), allClueLongs.get(currentClue - 1));
        } else if(currentClue == 7) {
            Toast.makeText(getActivity(), "YOU WIN", Toast.LENGTH_LONG).show();
            video.setVisibility(View.GONE); //get rid of video
            Drawable pmills = getActivity().getResources().getDrawable(R.drawable.olin_challenge);
            finale.setImageDrawable(pmills);
            return;
        }

        //set up video view and media controller
        uri = Uri.parse(s3.download(videoName));
        video.setVideoURI(uri);
        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.start();
            }
        });

        currentClue += 1;
    }


    //uploads a picture to amazon s3
    public void uploadPicture(String objectKey, String fileName){
        HttpHandler handler = new HttpHandler(getActivity().getApplicationContext());
        String clueInfo = "Picture of clue " + currentClue;
        //s3.upload(objectKey, fileName, clueInfo);
        handler.uploadImage(objectKey, currentClue - 1);
    }

    //request permission from phone to use GPS functionality
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {
                //if request is cancelled, the result arrays are empty.
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
            //other 'case' lines to check for other permissions this app might request
        }
    }

    //makes the camera icon clickable to let you take a picture
    public void setCameraButton(){
        //make button clickable
        images.get(imageIndex).setClickable(true);
        //make button purple
        images.get(imageIndex).setImageDrawable(purpleCamera);

        images.get(imageIndex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //let you take picture
                dispatchTakePictureIntent();
            }
        });
    }

    //makes camera button unclickable. meant for if you go to the clue then leave it, so you can't still take pictures
    public void dontSetCameraButton(){
        // make button not clickable
        images.get(imageIndex).setClickable(false);
        // make button black
        images.get(imageIndex).setImageDrawable(blackCamera);
    }

    //shows the image when clicked in a window
    public void setImageDialog(){
        if (photoUriList != null) {
            for (int i = 0; i < photoUriList.size(); i++) {
                if (i != imageIndex) {
                    final Uri u = photoUriList.get(i);
                    images.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                InputStream inputStream = getActivity().getBaseContext().getContentResolver().openInputStream(u);
                                fullImage = Drawable.createFromStream(inputStream, u.toString());
                            } catch (FileNotFoundException e) {

                            }
                            final Dialog nagDialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog.setCancelable(false);
                            nagDialog.setContentView(R.layout.preview_image);
                            Button btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
                            ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                            ivPreview.setBackgroundDrawable(fullImage);

                            btnClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                     nagDialog.dismiss();
                                }
                            });
                            nagDialog.show();
                        }
                    });
                    if(imageIndex != 6) { //in order to prevent errors when it tries to get "clue" 7 (which here is 6)
                        images.get(imageIndex).setClickable(false);
                    }
                }
            }
        }
    }

    //for after you take a picture to replace camera icons
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            images.get(imageIndex).setImageBitmap(imageBitmap);
            Uri imageUri = data.getData();
            photoUriList.add(imageIndex, imageUri);
            UUID uid = UUID.randomUUID();
            uploadPicture(uid.toString(), imageUri.toString());
            downloadClue(); //TODO: if yes download
            imageIndex ++;
            locationListener.doneWithClue();
            setImageDialog();
            }
    }

    //tells phone to take a picture
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
