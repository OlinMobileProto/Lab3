package com.example.cynchen.scavengerhunt.HomeScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.cynchen.scavengerhunt.MainActivity;
import com.example.cynchen.scavengerhunt.R;
import com.example.cynchen.scavengerhunt.Video.VideoFragment;


public class HomeScreenFragment extends Fragment {


    public Button start_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //fragment_home_screen is the layout
        final View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        //start button starts the first clue in video fragment
        start_button = (Button) rootView.findViewById(R.id.startbutton);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoFragment videofrag = new VideoFragment();
                ((MainActivity)getActivity()).transitionToFragment(videofrag);
            }
        });

        return rootView;
    }
}
