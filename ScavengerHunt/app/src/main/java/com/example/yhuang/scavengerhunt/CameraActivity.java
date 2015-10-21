package com.example.yhuang.scavengerhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        onLaunchCamera(this.findViewById(android.R.id.content).getRootView());
    }

    /**
     * When "onLaunchCamera" is called, an implicit
     * intent will be sent out to camera. Data containing
     * image will be returned to "onActivityResult" when
     * the camera is done
     */
    public void onLaunchCamera(View view) {
        // create an implicit Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override

    /**
     * "onActivityResult" will be called when the camera
     * intent returns data the requestCode and resultCode
     * will be sent back for status checking and
     * the data is stored in "data"
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode != RESULT_CANCELED && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap takenImage = (Bitmap) extras.get("data");

                    //uploadImageToDatabase(Bitmap takenImage);

                    ImageView ivPreview = (ImageView) findViewById(R.id.imageView_test);
                    ivPreview.setImageBitmap(takenImage);
        } else {
            Toast.makeText(this, R.string.no_picture, Toast.LENGTH_SHORT).show();
        }
    }

}
