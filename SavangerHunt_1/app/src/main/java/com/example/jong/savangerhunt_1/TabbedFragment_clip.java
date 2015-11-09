package com.example.jong.savangerhunt_1;

import android.app.ProgressDialog;;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

/**
 * Created by root on 10/22/15.
 */
public class TabbedFragment_clip extends android.support.v4.app.Fragment {

    public static final String ARG_SECTION_NUMBER = "1";
    private StageData stageData;
    private VideoView mVideoView;
    private TextView textview;
    private TextView stageprogress;
    private ImageView imageView;


    public TabbedFragment_clip() {
    }

    public void create_videoview(View v) {
        final int videopos = 0;
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Scavanger Hunt Clue Video");
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        MediaController mediaControls = new MediaController(getContext());
        mVideoView = (VideoView) v.findViewById(R.id.video_view);
        mVideoView.setMediaController(mediaControls);
        Log.d("currentstage", String.valueOf(stageData.getCurrstage()));
        String s3url = stageData.getVideoURI(stageData.getCurrstage());
        mVideoView.setVideoURI(Uri.parse(s3url));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                mVideoView.start();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cliplayout, container, false);
        stageData= new StageData(this.getActivity());
        create_videoview(view);
        textview = (TextView) view.findViewById(R.id.stage_text);
        textview.setText("    Stage # ".concat(String.valueOf(stageData.getVisiblestage())));
        stageprogress = (TextView) view.findViewById(R.id.Stage_status);
        imageView = (ImageView) view.findViewById(R.id.completed_image);
        return view;
    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container_frame, fragment);
        transaction.commit();
    }
    public void updateclipview(int stage){
        String newvideourl = stageData.getVideoURI(stage);
        String completedimg=stageData.getImageURI(stage);
        imageView.setImageURI(Uri.parse(completedimg));
        mVideoView.setVideoURI(Uri.parse(newvideourl));
        textview.setText("    Stage # ".concat(String.valueOf(stage)));
    }

}