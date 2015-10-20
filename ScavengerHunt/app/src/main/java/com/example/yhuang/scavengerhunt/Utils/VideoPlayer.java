package com.example.yhuang.scavengerhunt.Utils;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer {

    public static void playVideo (String videoName, VideoView clueVideo, Activity activity) {
        try {
            //build the uri with the base and the input videoName
            String base = "https://s3.amazonaws.com/olin-mobile-proto/";
            base += videoName;
            Uri uri = Uri.parse(base);

            //set up the mediaController to play the video
            MediaController mediaController = new MediaController(activity);
            mediaController.setAnchorView(clueVideo);
            clueVideo.setMediaController(mediaController);
            clueVideo.setVideoURI(uri);
            clueVideo.start();
        } catch (Exception err) {
            Log.e("Error: ", err.toString());
        }
    };
}
