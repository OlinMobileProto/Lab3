package com.example.lwilcox.thehunt;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nmohamed on 10/10/2015.
 */
public class AmazonS3 extends AsyncTask<Void,Void,Void> {
    private Context context;
    public TransferUtility transferUtility;

    public AmazonS3 (Context mContext){
        context = mContext;
    }

    @Override
    protected Void doInBackground(Void... params) {
        AmazonS3Client s3 = new AmazonS3Client(new BasicAWSCredentials("AKIAISEFKD6O3QSZGHUQ", "ETum1qfRaUFQ/ixydMBA+yBcUJLY5m8/JojEufNf"));
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, context.getApplicationContext());

        return null;
    }

    public void upload(String file_name, int clue_num, String clue_info){
        String object_key = "clue_" + clue_num;
        File file = new File(file_name);

        //metadata for future implementations if you wanted to retrieve data and know what it was
        ObjectMetadata myObjectMetadata = new ObjectMetadata();
        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put(object_key, clue_info);
        myObjectMetadata.setUserMetadata(userMetadata);
        TransferObserver observer = transferUtility.upload(
               "olin-mobile-proto",     /* The bucket to upload to */
                object_key,    /* The key for the uploaded object */
                file,        /* The file where the data to upload exists */
                myObjectMetadata //ObjectMetadata associated with the object
        );
    }

    public void download(int clue_num){
        String object_key =  "clue_" + clue_num;
        File file = new File(object_key + ".MOV");
        TransferObserver observer = transferUtility.download(
                "olin-mobile-proto",     /* The bucket to download from */
                object_key,    /* The key for the object to download */
                file        /* The file to download the object to */
        );
    }
}
