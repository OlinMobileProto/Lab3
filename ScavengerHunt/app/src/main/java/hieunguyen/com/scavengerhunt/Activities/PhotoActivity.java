package hieunguyen.com.scavengerhunt.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hieunguyen.com.scavengerhunt.Fragments.CameraFragment;
import hieunguyen.com.scavengerhunt.Fragments.ClueCompleteFragment;
import hieunguyen.com.scavengerhunt.R;

public class PhotoActivity extends AppCompatActivity implements CameraFragment.OnFragmentChangeListener{
    private static final String TAG = PhotoActivity.class.getName();
    private static final String BUCKET_NAME = "olin-mobile-proto";

    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CameraFragment())
                    .commit();
        }
    }

    @Override
    public void onCameraButton() {
        dispatchTakePictureIntent();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                new S3Uploader().execute(photoFile);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new ClueCompleteFragment()).commit();
            }
        }
    }

    private class S3Uploader extends AsyncTask<File,Void,Void> {
        @Override
        protected Void doInBackground(File... params) {

            File fileToUpload = params[0];

            AmazonS3 s3Client = new AmazonS3Client();

            PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, fileToUpload.getName(),
                    fileToUpload);
            PutObjectResult putResponse = s3Client.putObject(putRequest);

            return null;
        }

    }

}
