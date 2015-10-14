package com.example.yhuang.scavengerhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLaunchCamera(View view) {
        // create an implicit Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    /*
     * "onActivityResult" will be called when the camera intent returns data
     * the requestCode and resultCode will be sent back for status checking
     * and the data is stored in "data"
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode != RESULT_CANCELED && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap takenImage = (Bitmap) extras.get("data");

                    //uploadImageToDatabase(Bitmap takenImage);

                    //ImageView ivPreview = (ImageView) findViewById(R.id.imageView_test);
                    //ivPreview.setImageBitmap(takenImage);
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

}
