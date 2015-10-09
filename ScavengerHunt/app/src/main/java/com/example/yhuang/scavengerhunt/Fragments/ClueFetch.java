package com.example.yhuang.scavengerhunt.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.yhuang.scavengerhunt.R;

public class ClueFetch extends Fragment {

    TextView curClue;
    TextView totalClue;
    VideoView clueVideo;
    ImageButton prev;
    ImageButton next;
    ImageButton camera;

    public ClueFetch() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_clue_fetch, container, false);

        curClue = (TextView) rootView.findViewById(R.id.currentClue);
        totalClue = (TextView) rootView.findViewById(R.id.totalClue);
        clueVideo = (VideoView) rootView.findViewById(R.id.videoClue);
        camera = (ImageButton) rootView.findViewById(R.id.camera);
        prev = (ImageButton) rootView.findViewById(R.id.prev);
        next = (ImageButton) rootView.findViewById(R.id.next);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }


}
