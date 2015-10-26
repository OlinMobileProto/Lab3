package com.example.lwilcox.thehunt;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Amazon S3: Handles uploading to S3. Uploads the pictures that you take to S3
 */

public class AmazonS3 {
    private Context context;
    public TransferUtility transferUtility;

    public AmazonS3 (Context mContext){
        context = mContext;
    }

    public void upload(String objectKey ,String fileName, String clueInfo){
        File file = new File(fileName);
        AmazonS3Client s3 = new AmazonS3Client();
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, context);

        //metadata for future implementations if you wanted to retrieve data and know what it was
        ObjectMetadata myObjectMetadata = new ObjectMetadata();
        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put(objectKey, clueInfo);
        myObjectMetadata.setUserMetadata(userMetadata);

        //upload
        TransferObserver observer = transferUtility.upload(
               "olin-mobile-proto",     /* The bucket to upload to */
                objectKey,    /* The key for the uploaded object */
                file,        /* The file where the data to upload exists */
                myObjectMetadata //ObjectMetadata associated with the object
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d("State changed", "The state of the uploading Transfer Observer has changed...");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.d("Percent", percentage + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("Error", "An error occurred when downloading from S3");
                ex.printStackTrace();
            }
        });
    }

    public String download(String clueName){
        return "https://s3.amazonaws.com/olin-mobile-proto/" + clueName;
    }
}
