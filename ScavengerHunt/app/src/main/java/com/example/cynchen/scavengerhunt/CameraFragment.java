package com.example.cynchen.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class CameraFragment extends Fragment {

    public Button tempopenCamera;
    public Button saveimage;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
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

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            }
        });

        //for now: saveimage only transitions to next clue, but later it will do some stuff with the database
        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VideoFragment camera_frag = new VideoFragment();
                ((MainActivity)getActivity()).transitionToFragment(camera_frag);
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
