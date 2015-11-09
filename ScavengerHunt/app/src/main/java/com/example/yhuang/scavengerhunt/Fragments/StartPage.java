package com.example.yhuang.scavengerhunt.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yhuang.scavengerhunt.R;
import com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch.NewGameListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This is the opening page with New Game button
 */
public class StartPage extends Fragment{

    @Bind(R.id.new_game_but) Button newGame;

    public StartPage() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_start_page, container, false);

        Activity activity = getActivity(); //get the activity
        ButterKnife.bind(this, rootView); // bind butter knife

        newGame.setOnClickListener(new NewGameListener(activity));
        return rootView;
    }

}
