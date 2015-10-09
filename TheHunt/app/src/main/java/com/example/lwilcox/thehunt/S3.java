package com.example.lwilcox.thehunt;

import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

/**
 * Created by lwilcox on 10/8/2015.
 */
public class S3 extends AsyncTask<Void , Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        AmazonS3Client s3Client = new AmazonS3Client();
        File fileToUpload =
        return null;
    }
}
