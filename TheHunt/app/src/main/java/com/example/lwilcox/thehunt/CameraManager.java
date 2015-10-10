package com.example.lwilcox.thehunt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lwilcox on 10/10/2015.
 */
public class CameraManager extends Camera {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    public Context mContext;

    public CameraManager(Context context){
        mContext = context;
    }

    public void dispatchTakePictureIntent() {
        //this.context = context.getApplicationContext();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            ((Activity) mContext).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //@Override
    public Bitmap onActivityResult(int requestCode, int resultCode, Intent data, Context mcontext) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == ((Activity) mcontext).RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            return imageBitmap;
        }
        return null;
    }

    public File createImageFile() throws IOException{
        //actually we probably just want to save to S3 from here.
        UUID uid = UUID.randomUUID();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //used as name

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                timeStamp,             /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
