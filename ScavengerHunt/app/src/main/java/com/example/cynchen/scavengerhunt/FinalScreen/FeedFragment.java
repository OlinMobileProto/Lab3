package com.example.cynchen.scavengerhunt.FinalScreen;

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

import com.example.cynchen.scavengerhunt.R;

import java.io.File;

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

    //displays to the user a feed of all of the photos they took on their scavenger hunt. This
    //implementation uses the most recent 6 photos in the user's gallery which makes sense if the
    //user does the scavenger hunt in one go, better implementation would be to use the web server
    //to get the 6 most recent uploads and use the ids to display the 6 photos published to s3
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        next_button = (Button) rootView.findViewById(R.id.next_button);
        prev_button = (Button) rootView.findViewById(R.id.prev_button);
        imageView = (ImageView) rootView.findViewById(R.id.feedView);
        imgCounter = 0; //only interested in showing the user the 6 most recent photos from their gallery,
        //start with the most recent one

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

        //now that we have the most recent photo, put it in the imageView
        if (cursor.moveToPosition(imgCounter)) {
            String imageLocation = cursor.getString(1);
            imageFile = new File(imageLocation);
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }

        //on click of next button, move to the second to most recent photo in the gallery on the phone
        //wrap around to the most recent photo to only display the 6 photos taken on the scavenger hunt
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCounter ++;
                if (imgCounter > 5){
                    imgCounter = 0;
                }
                //we keep moving the cursor and updating the imageView, no visible delay in experience
                //but could potentially only use the cursor once, store the 6 photos in an arrayList
                //and then not have to move the cursor
                if (cursor.moveToPosition(imgCounter)) {
                    String imageLocation = cursor.getString(1);
                    imageFile = new File(imageLocation);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }
            }
        });

        //same thing as next button except loops the other way from less recent photos to most recent,
        //again only displaying the 6 most recent photos taken on the scavenger hunt
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
