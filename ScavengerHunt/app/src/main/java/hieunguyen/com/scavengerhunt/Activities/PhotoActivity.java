package hieunguyen.com.scavengerhunt.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

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
    private File mPhotoFile;


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

    /*
    Creates an image file with name of current time as UUID

    Returns:
        image (File): The created image file
     */
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

    /*
    Creates and launches Intent to open the Camera, and retrieve picture
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            mPhotoFile = null;
            try {
                mPhotoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, ex.toString());
            }
            // Continue only if the File was successfully created
            if (mPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*
    Overrides onActivityResult to be called when the picture has been taken
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Upload file to S3
        new S3Uploader().execute();
        // Create ClueCompleteFragment
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new ClueCompleteFragment()).commit();
    }

    /*
    Inner AsyncTask to deal with uploading to S3 asynchronously
     */
    private class S3Uploader extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // Create AmazonS3Client
            AmazonS3Client s3Client = new AmazonS3Client();
            s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));

            TransferUtility mTransferUtility = new TransferUtility(s3Client, PhotoActivity.this);

            TransferObserver observer = mTransferUtility.upload(
                    "olin-mobile-proto",      // Bucket name
                    mPhotoFile.getName(),    // Uploaded object key
                    mPhotoFile              // File to be uploaded
            );

            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, state.toString());
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    int completionPercentage = (int) (bytesCurrent/bytesTotal)*100;
                    Log.d(TAG, completionPercentage + " percent complete");
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, ex.toString());
                    ex.printStackTrace();
                }
            });
            return null;
        }
    }
}
