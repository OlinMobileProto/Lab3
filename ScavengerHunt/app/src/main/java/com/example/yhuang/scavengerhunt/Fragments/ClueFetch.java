package com.example.yhuang.scavengerhunt.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.yhuang.scavengerhunt.MainActivity;
import com.example.yhuang.scavengerhunt.R;
import com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch.CameraListener;
import com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch.NextListener;
import com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch.PreviousListener;
import com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch.VideoListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * In ClueFetch fragment, we first use
 * butter knife to connect view with our
 * model, set up GPS for location checking,
 * and a package manager to check whether
 * the camera is available.
 *
 * Four onClickListeners are setup here
 * and get further implemented in the
 * onClickListeners/ClueFetch folder
 */

public class ClueFetch extends Fragment {

    public static int curClueNum = 1;
    private static int firstRules = 1; //Reading the rules for the first time

    @Bind(R.id.currentClue) TextView curClue;
    @Bind(R.id.videoClue) VideoView clueVideo;
    @Bind(R.id.camera) ImageButton camera;
    @Bind(R.id.prev) ImageButton prev;
    @Bind(R.id.next) ImageButton next;
    @Bind(R.id.video) ImageButton video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_clue_fetch, container, false);
        final Activity activity = getActivity(); //get the activity
        ButterKnife.bind(this, rootView); // bind butter knife
        if (firstRules == 1) {
            final AlertDialog.Builder adb = new AlertDialog.Builder(activity);
            adb.setTitle("Instructions");
            adb.setMessage("For each of the six clues, watch the video");
            adb.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adb.setMessage("After going to the place, take a picture.");
                    adb.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adb.setMessage("Upload the picture to complete the level");
                            adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getActivity(), "You are all set", Toast.LENGTH_SHORT).show();
                                }
                            });
                            adb.show();
                        }
                    });
                    adb.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Do Nothing
                        }
                    });
                    adb.show();
                }
            });
            adb.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                }
            });
            adb.show();
            firstRules = 0;
        }
        GpsDetection gpsInfo = new GpsDetection(rootView.getContext()); // get the GPS detection fragment
        PackageManager packageManager = activity.getPackageManager(); // get the Package Manager

        curClue.setText(Integer.toString(curClueNum));
        clueVideo.setVisibility(View.INVISIBLE); //hide the video initially and reveal it when a play button is hit

        // set up onClickListeners
        video.setOnClickListener(new VideoListener(curClueNum, video, clueVideo, activity));
        camera.setOnClickListener(new CameraListener(gpsInfo, activity, packageManager,curClueNum));
        prev.setOnClickListener(new PreviousListener(activity, this));
        next.setOnClickListener(new NextListener(activity, this));
        return rootView;
    }

    public void nextClue() {
        if (curClueNum >= 6) {
            Toast.makeText(getActivity(), R.string.last_clue, Toast.LENGTH_SHORT).show();
        } else {
            curClueNum++;
            ((MainActivity) getActivity()).changeToClue();
        }
    }

    public void prevClue() {
        if (curClueNum <= 1) {
            Toast.makeText(getActivity(), R.string.first_clue, Toast.LENGTH_SHORT).show();
        } else {
            curClueNum--;
            ((MainActivity) getActivity()).changeToClue();
        }
    }
}
