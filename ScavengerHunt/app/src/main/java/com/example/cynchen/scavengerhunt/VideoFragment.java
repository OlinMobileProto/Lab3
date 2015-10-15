package com.example.cynchen.scavengerhunt;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;


public class VideoFragment extends Fragment {

    public TextView clueTitle;
    public VideoView clue;
    public ArrayList<String> cluesLink = new ArrayList<>();
    public int clueCounter;
//    public Button tempnextClue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3146.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3145.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3144.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3147.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3141.3gp");
        cluesLink.add("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp");
        clueCounter = 1;
        final View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        clueTitle = (TextView) rootView.findViewById(R.id.clueTitle);
        clueTitle.setText("Clue " + Integer.toString(clueCounter));
        clue = (VideoView) rootView.findViewById(R.id.clue);
        clue.setVideoURI(Uri.parse(cluesLink.get(clueCounter - 1)));
        clue.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                clue.seekTo(1);
//                MediaController mc = new MediaController(getContext()) {
//                    @Override
//                    public void hide() {
//                    }
//                };
//                clue.setMediaController(mc);
//                mc.setAnchorView(rootView.findViewById(R.id.mc_wrapper));
                MediaController mc = new MediaController(getContext());
                clue.setMediaController(mc);
                mc.setAnchorView(clue);
            }
        });
        clue.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                GPSFragment gps_frag = new GPSFragment();
                ((MainActivity)getActivity()).transitionToFragment(gps_frag);
            }
        });
//        tempnextClue = (Button) rootView.findViewById(R.id.next_clue);
//        tempnextClue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clueCounter += 1;
//                clueTitle.setText("Clue " + Integer.toString(clueCounter));
//                clue.setVideoURI(Uri.parse(cluesLink.get(clueCounter - 1)));
//            }
//        });

        return rootView;
    }
}
