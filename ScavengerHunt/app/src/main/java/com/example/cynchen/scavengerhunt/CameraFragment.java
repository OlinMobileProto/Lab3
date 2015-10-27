package com.example.cynchen.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class CameraFragment extends Fragment {

    public Button tempopenCamera;
    public Button saveimage;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
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
        tempopenCamera = (Button) rootView.findViewById(R.id.camera_button);

        //Save Image Button
        saveimage = (Button) rootView.findViewById(R.id.save_button);

        tempopenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);

                //HAVE if statement to check if activity manager can be initialized

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });

        //for now: saveimage only transitions to next clue, but later it will do some stuff with the database
        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
                uid = uid.randomUUID();
                Log.d("UUID: ", uid.toString());
                File imageFile = null;

                // Find the last picture
                String[] projection = new String[]{
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.MIME_TYPE
                };
                final Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

                // Put it in the image view
                if (cursor.moveToFirst()) {
                    String imageLocation = cursor.getString(1);
                    imageFile = new File(imageLocation);
                }

                if (imageFile != null) {
                    Log.d("SUCCESS!", imageFile.toString());
                    BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAISEFKD6O3QSZGHUQ", "ETum1qfRaUFQ/ixydMBA+yBcUJLY5m8/JojEufNf");

                    AmazonS3Client s3Client = new AmazonS3Client(awsCreds);
                    TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
                    TransferObserver observer = transferUtility.upload("olin-mobile-proto", uid.toString(), imageFile);
                }

                VolleyRequest handler = new VolleyRequest(getActivity().getApplicationContext());
                handler.putID(uid, ((MainActivity) getActivity()).return_counter());

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
        //WE WILL USE THE LAST PICTURE OF THE GALLERY TO GET THE PICTURE THAT WE ARE SAVING TO DATABASE
        return rootView;
    }

    //What happens once image has been taken
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //saveimage button only pops up once an image is uploaded
                saveimage.setVisibility(View.VISIBLE);

//                Uri contentUri = data.getData();
//                Log.d("uri", contentUri.toString());
//
//
//                imageView.setImageURI(contentUri);
//                //Do getData instead, Uri contenturi
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                imageView.setImageBitmap(bitmap);

            }
        }
    }
}
