package com.example.yhuang.scavengerhunt;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.yhuang.scavengerhunt.Database.ClueDBConnection;
import com.example.yhuang.scavengerhunt.Utils.LocalUUID;
import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private int curClueNum;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent mIntent = getIntent();
        curClueNum = mIntent.getIntExtra(getResources().getString(R.string.curClueNum), 0);

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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException err) {
                // Error occurred while creating the File
                err.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
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
            Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_LONG).show();
            String uuid = LocalUUID.getUUID();
            Upload(mCurrentPhotoPath, uuid);
            //new S3.UploadImage().execute(mCurrentPhotoPath, uuid);

        } else {
            Toast.makeText(this, R.string.no_picture, Toast.LENGTH_SHORT).show();
        }


        Intent MainActivity = new Intent(this, MainActivity.class);
        MainActivity.putExtra("Class", "CameraActivity");
        this.startActivity(MainActivity);
    }

    /**
     * "Upload" take the file path and an uuid,
     * and upload the image to S3
     */

    public void Upload (String filename, String uuid) {
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(getResources().getString(R.string.AccessKeyId), getResources().getString(R.string.Secret_Access_Key)));
        TransferUtility transferUtility = new TransferUtility(s3Client,this);
        ClueDBConnection clueDBConnection = new ClueDBConnection(this);
        File fileToUpload = new File(filename);

        transferUtility.upload(
                getResources().getString(R.string.s3bucket),     /* The bucket to upload to */
                uuid,    /* The key for the uploaded object */
                fileToUpload        /* The file where the data to upload exists */
        );

        clueDBConnection.postIds(uuid, Integer.toString(curClueNum));
    }

    /**
     * "createImageFile" create a new file
     * from the phone's storage
     */

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = getResources().getString(R.string.file_name);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                getResources().getString(R.string.suffix),         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
