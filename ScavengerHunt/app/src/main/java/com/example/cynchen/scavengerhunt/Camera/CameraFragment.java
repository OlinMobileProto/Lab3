package com.example.cynchen.scavengerhunt.Camera;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.cynchen.scavengerhunt.FinalScreen.FeedFragment;
import com.example.cynchen.scavengerhunt.MainActivity;
import com.example.cynchen.scavengerhunt.R;
import com.example.cynchen.scavengerhunt.Video.VideoFragment;
import com.example.cynchen.scavengerhunt.VolleyRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class CameraFragment extends Fragment {

    public Button openCamera;
    public Button saveimage;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    public UUID uid;
    public File imageFile;
    public ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.imageview);
        //Opens Camera for the user to take a picture of the location
        openCamera = (Button) rootView.findViewById(R.id.camera_button);

        //Save Image Button
        saveimage = (Button) rootView.findViewById(R.id.save_button);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Try to start camera activity when open camera button is pressed
                try{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,
                            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (Exception e){
                    Log.e("ERROR!", e.getMessage());
                }
            }
        });

        //save image button transitions to the next fragment, and puts the picture into s3
        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generates random UUID to identify each image
                uid = uid.randomUUID();
                Log.d("UUID: ", uid.toString());

                //creates the cursor to store query to MediaStore on phone
                String[] projection = new String[]{
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.MIME_TYPE
                };
                final Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

                //sets the imageFile to be the most recent photo in the phone's gallery
                if (cursor.moveToFirst()) {
                    String imageLocation = cursor.getString(1);
                    imageFile = new File(imageLocation);
                }
                //once we have the most recent photo, upload it to s3
                if (imageFile != null) {
                    BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAISEFKD6O3QSZGHUQ", "ETum1qfRaUFQ/ixydMBA+yBcUJLY5m8/JojEufNf");
                    AmazonS3Client s3Client = new AmazonS3Client(awsCreds);
                    TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
                    TransferObserver observer = transferUtility.upload("olin-mobile-proto", uid.toString(), imageFile);
                }
                //also have to upload the id and the location to the web server using Volley
                VolleyRequest handler = new VolleyRequest(getActivity().getApplicationContext());
                handler.putID(uid, ((MainActivity) getActivity()).return_counter());

                //Checks to see if it's on the last clue, then it will show the final page
                //ELSE: increment to the next clue video fragment
                if (((MainActivity)getActivity()).return_counter() == 6) {
                    FeedFragment feed_frag = new FeedFragment();
                    ((MainActivity) getActivity()).transitionToFragment(feed_frag);
                }
                else {
                    VideoFragment camera_frag = new VideoFragment();
                    ((MainActivity) getActivity()).transitionToFragment(camera_frag);
                }
            }
        });

        return rootView;
    }

    //What happens once image has been taken
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //publish image button only pops up once an image is uploaded
                saveimage.setVisibility(View.VISIBLE);

                //make bitmap of a thumbnail of the image that was just taken
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                //set imageview to be the thumbnail
                imageView.setImageBitmap(bitmap);

            }
        }
    }
}
