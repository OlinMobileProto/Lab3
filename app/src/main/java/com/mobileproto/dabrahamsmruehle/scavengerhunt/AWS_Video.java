package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.app.Activity;
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

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AWS_Video.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AWS_Video#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AWS_Video extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public AmazonS3 s3; // unnecessary
    private VideoView videoView;
    private MediaController mediaController;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AWS_Video.
     */
//    // TODO: Rename and change types and number of parameters
    public static AWS_Video newInstance() {
        AWS_Video fragment = new AWS_Video();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AWS_Video() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        s3 = new AmazonS3Client(); // deprecated apparently?
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout videoPlayerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_aws__video, container, false);
        videoView = (VideoView) videoPlayerLayout.findViewById(R.id.video_view);
        Toast.makeText(getActivity(), "got to here", Toast.LENGTH_SHORT).show();
        mediaController = new MediaController(getActivity());
        videoView.setMediaController(mediaController);

        Uri uriToPlay = Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp");
        videoView.setVideoURI(uriToPlay);
        videoView.requestFocus();
        mediaController.show();
        videoView.start();

        return videoPlayerLayout;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void playVideo(Uri videoUri) {
        Uri uriToPlay = Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp");
        videoView.setVideoURI(uriToPlay);
        videoView.requestFocus();
        mediaController.show();
        videoView.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
