package com.mobileproto.jovanduy.scavengerhunt;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.util.UUID;

/**
 * Class to upload photos using AsynchTask to S3
 */
public class S3Upload extends AsyncTask<Void, Integer, Void> {

    static final String BUCKET_NAME = "olin-mobile-proto";
    static final String ACCESS_KEY = "AKIAISEFKD6O3QSZGHUQ";
    static final String SECRET_KEY = "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";

    private Context context;
    private File file;
    private UUID uuid;
    private ProgressDialog dialog;
    private int stage;
    private FragmentActivity activity;

    public S3Upload(Context context, File file, UUID uuid, int stage, FragmentActivity activity) {
        this.context = context;
        this.file = file;
        this.uuid = uuid;
        this.stage = stage;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        // Set up progress dialog
        dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.stage) + "Image");
        dialog.setMessage("Uploading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3Client s3Client = new AmazonS3Client();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, uuid.toString(), file);
            PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);
            final int[] total = {0};
            putObjectRequest.setProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    total[0] += (int) progressEvent.getBytesTransferred();
                    publishProgress(total[0]);
                }
            });
        } catch (Exception e) {
            Log.e("ERROR!!!", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        Server server = new Server(context);
        server.postImage(uuid.toString(), stage + 1, new PutCallback() {
            @Override
            public void callbackPut(boolean success, String statusCode) {
                Log.d("IMAGE UPLOADED?", statusCode.toString());
            }
        });
        MainActivity mainActivity = (MainActivity) activity;
        if (mainActivity.videoFragment.getHuntProgress().isOnLastStage()) {
            GameEnd gameEnd = new GameEnd();
            transitionToFragment(gameEnd);
        } else {
            SectionEnd sectionEndFragment = new SectionEnd();
            transitionToFragment(sectionEndFragment);
        }
    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = activity.getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}