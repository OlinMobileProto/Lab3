package com.example.jong.savangerhunt_1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 10/27/15.
 */
public class Photoview extends android.support.v4.app.Fragment {

    private String TAG = "PhotoView";
    private Uri fileUri;
    private ImageView imgView;
    private Button ok;
    private Button retake;
    private Button cancel;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int currstage;
    private static String root = null;
    private static String imageFolderPath=null;
    private StageData stageData;
    private notifystagefinished finishedstage;
    private int finalstage =6;

    public Photoview() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        stageData=new StageData(this.getActivity());
        View view = inflater.inflate(R.layout.photoview, container, false);
        createImageview(view);
        createbutton(view, "Ok");
        createbutton(view, "Retake");
        return view;
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof notifystagefinished){
            finishedstage=(notifystagefinished)activity;
        }

    }

    public void createImageview(View v){
        if(stageData.getImageURI(stageData.getVisiblestage())=="nothing"){
            fileUri=null;
        }
        else{
            fileUri=Uri.parse(stageData.getImageURI(stageData.getVisiblestage()));
        }
        imgView = (ImageView) v.findViewById(R.id.imageview);

        if(fileUri==null){
            imgView.setImageResource(R.drawable.ic_cam);
        }
        else{
            Bitmap bitmap;
            try {
                GetImageThumbnail getImageThumbnail = new GetImageThumbnail();
                bitmap = getImageThumbnail.getThumbnail(fileUri, getActivity());
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void createbutton(View v, String button){
        switch (button) {
            case("Ok"): {
                ok = (Button) v.findViewById(R.id.okbutton);
                if (fileUri==null){
                    ok.setEnabled(false);
                }
                else{
                    ok.setEnabled(true);
                }
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stageData.getCurrstage()==finalstage){
                            transitionToFragment(new Finished_frag());
                        }
                        else {
                            stageData.finishedcurrstage();
                            stageData.setImageURIs(fileUri.toString());
                            finishedstage.updatemainview(stageData.getCurrstage());
                            transitionToFragment(new ViewpagerContainer());
                        }
                    }
                });
            }
            case("Retake"):{
                retake = (Button) v.findViewById(R.id.retakebutton);
                if(fileUri==null){
                    retake.setText("Take Photo");
                }
                retake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captureImage();

                    }
                });
            }
            case("Cancel"):{
                cancel = (Button) v.findViewById(R.id.cancelbutton);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transitionToFragment(new ViewpagerContainer());
                    }
                });
            }
        }
    }
    public void captureImage(){
        String imageName;
        // fetching the root directory
        root = Environment.getExternalStorageDirectory().toString()
                + "/Scavangerhunt";

        // Creating folders for Image

        imageFolderPath = root;
        File imagesFolder = new File(imageFolderPath);
        if(!imagesFolder.exists()){
            imagesFolder.mkdirs();
        }

        // Generating file name
        imageName = "stage"+String.valueOf(currstage)+".jpg";
        // Creating image here
        File image = new File(imageFolderPath, imageName);
        fileUri = Uri.fromFile(image);
        dispatchTakePictureIntent(image);
    }

    public void dispatchTakePictureIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:

                    Bitmap bitmap = null;
                    try {
                        GetImageThumbnail getImageThumbnail = new GetImageThumbnail();
                        bitmap = getImageThumbnail.getThumbnail(fileUri, getActivity());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    imgView.setImageBitmap(bitmap);
                    ok.setEnabled(true);
                    break;
                default:
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container_frame, fragment);
        transaction.commit();
    }
}
