package com.example.cynchen.scavengerhunt;

import android.database.Cursor;
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

import java.io.File;

/**
 * Created by faridaghadyali on 10/26/15.
 */
public class FeedFragment extends Fragment {

    public Button next_button;
    public Button prev_button;
    public ImageView imageView;
    public int imgCounter;
    public File imageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //oncreateview: finds the correct clueslink video to show dependent on the clue counter from main activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        next_button = (Button) rootView.findViewById(R.id.next_button);
        prev_button = (Button) rootView.findViewById(R.id.prev_button);
        imageView = (ImageView) rootView.findViewById(R.id.feedView);
        imgCounter = 0;

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
//        if (cursor.moveToFirst()) {
//            String imageLocation = cursor.getString(1);
//            imageFile = new File(imageLocation);
//            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//            imageView.setImageBitmap(myBitmap);
//        }

        if (cursor.moveToPosition(imgCounter)) {
            String imageLocation = cursor.getString(1);
            imageFile = new File(imageLocation);
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCounter ++;
                if (imgCounter > 5){
                    imgCounter = 0;
                }
                if (cursor.moveToPosition(imgCounter)) {
                    String imageLocation = cursor.getString(1);
                    imageFile = new File(imageLocation);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
            }
        });

        prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCounter --;
                if (imgCounter < 0){
                    imgCounter = 5;
                }

                if (cursor.moveToPosition(imgCounter)) {
                    String imageLocation = cursor.getString(1);
                    imageFile = new File(imageLocation);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
            }
        });



        return rootView;

    }

}
