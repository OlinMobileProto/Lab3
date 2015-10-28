package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class AWS_Video extends Fragment
{
    private VideoView videoView;
    private MediaController mediaController;
    private OnFragmentInteractionListener mListener;

    public static AWS_Video newInstance()
    {
        AWS_Video fragment = new AWS_Video();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AWS_Video()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        LinearLayout videoPlayerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_aws__video, container, false);
        videoView = (VideoView) videoPlayerLayout.findViewById(R.id.video_view);
        Toast.makeText(getActivity(), "got to here", Toast.LENGTH_SHORT).show();
        mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);
        String urlToPlay = "https://s3.amazonaws.com/olin-mobile-proto/" + sharedPreferences.getString("target_video", "MVI_3146.3gp"); // gets target video; defaults to first clue just-in-case.
        Uri uriToPlay = Uri.parse(urlToPlay);
        videoView.setVideoURI(uriToPlay);
        videoView.requestFocus();
        mediaController.show();
        videoView.start();

        return videoPlayerLayout;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void playVideo(Uri videoUri)
    {
        Uri uriToPlay = Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp");
        videoView.setVideoURI(uriToPlay);
        videoView.requestFocus();
        mediaController.show();
        videoView.start();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(Uri uri);
    }

}
