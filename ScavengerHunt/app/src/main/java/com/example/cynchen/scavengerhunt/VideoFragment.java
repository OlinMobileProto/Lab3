package com.example.cynchen.scavengerhunt;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import java.util.ArrayList;


public class VideoFragment extends Fragment {

    public TextView clueTitle;
    public VideoView clue;
    public ArrayList<String> cluesLink = new ArrayList<>();
    private int clueCounter; //changed from public to private

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //oncreateview: finds the correct clueslink video to show dependent on the clue counter from main activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        

        cluesLink = ((MainActivity)getActivity()).locations_videos;

        //Calls increment_counter during initialization to get the number of clue that it is on after finishing the first clue
        clueCounter = ((MainActivity)getActivity()).increment_counter();
        Log.d("Counter", String.valueOf(clueCounter));

        //fragment_video is the layout
        final View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        //Setting the clue title to be aligned with what clue the user is on
        clueTitle = (TextView) rootView.findViewById(R.id.clueTitle);
        clueTitle.setText("Clue " + Integer.toString(clueCounter));

        //Setting the clue video to be the clueCounter - 1 video of the arraylist
        clue = (VideoView) rootView.findViewById(R.id.clue);
        clue.setVideoURI(Uri.parse(cluesLink.get(clueCounter - 1)));

        //Clue Media Controller code: COMMENT WHY WE HAVE THIS
        clue.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                clue.seekTo(1);
                MediaController mc = new MediaController(getContext());
                clue.setMediaController(mc);
                mc.setAnchorView(clue);
            }
        });

        //As soon as the video is complete, GPS fragment class opens
        clue.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                GPSFragment gps_frag = new GPSFragment();
                ((MainActivity)getActivity()).transitionToFragment(gps_frag);
            }
        });
        return rootView;
    }
}
