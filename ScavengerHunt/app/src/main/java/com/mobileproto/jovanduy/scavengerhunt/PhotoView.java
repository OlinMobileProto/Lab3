package com.mobileproto.jovanduy.scavengerhunt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.File;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TransferQueue;

public class PhotoView extends Fragment {

    private String TAG = "PhotoView";
    private  Uri uri;
    private ImageView imgView;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public PhotoView() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo__view, container, false);
        createImageview(rootView);
        createRetakeButton(rootView);
        createSubmitButton(rootView);
        return rootView;
    }

    public void createImageview(View v){
        Bundle bundle = this.getArguments();
        String uriString = bundle.getString("uri", null);
        Log.d("create_imageview", uriString);
        uri = Uri.parse(uriString);
        imgView = (ImageView)v.findViewById(R.id.imageView);
        imgView.setImageURI(uri);
        imgView.setRotation(90);
        
    }
    public void createRetakeButton(View v) {
        Log.d(TAG, "retakeButton");
        Button retakeButton;
        retakeButton = (Button) v.findViewById(R.id.Retake);
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        }
    public void createSubmitButton(View v) {
        Log.d(TAG, "submitButton");
        Button submitButton;
        submitButton = (Button) v.findViewById(R.id.Submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "submit");
                UUID uuid = UUID.randomUUID();

//                URI uriURI = URI.create(uri.toString());
                File file = new File(uri.getPath());
                MainActivity mainActivity = (MainActivity) getActivity();
                S3Service s3Service = new S3Service(getContext());
                s3Service.uploadFile(uuid.toString(), file);
                Server server = new Server(getContext());
                server.postImage(uuid.toString(), mainActivity.videoFragment.getHuntProgress().getCurrStage() + 1, new PutCallback() {
                    @Override
                    public void callbackPut(boolean success, String statusCode) {
                        Log.d("IMAGE UPLOADED?", statusCode.toString());
                    }
                });
                if (mainActivity.videoFragment.getHuntProgress().isOnLastStage()) {
                    GameEnd gameEnd = new GameEnd();
                    transitionToFragment(gameEnd);
                } else {
                    SectionEnd sectionEndFragment = new SectionEnd();
                    transitionToFragment(sectionEndFragment);
                }

//                S3Upload s3Upload = new S3Upload(getContext(), file, uuid, mainActivity.videoFragment.getHuntProgress().getCurrStage(), getActivity());
//                s3Upload.execute();
//                SectionEnd sectionEndFragment = new SectionEnd();
//                transitionToFragment(sectionEndFragment);
            }
        });
    }
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            uri = Uri.parse(data.getData().toString());
            imgView.setImageURI(uri);
        }
    }
    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}

