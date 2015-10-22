package hieunguyen.com.scavengerhunt.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hieunguyen.com.scavengerhunt.Activities.HuntActivity;
import hieunguyen.com.scavengerhunt.R;

public class ClueFragment extends Fragment {

    private onVideoDoneListener mListener;

    @Bind(R.id.done_button) ImageButton mDoneButton;
    @OnClick(R.id.done_button) void done(){
        mListener.onVideoDone();
    }

    @Bind(R.id.clue_vv) VideoView mClueVideoView;
    @Bind(R.id.title_tv) TextView mTitleTextView;

    ProgressDialog pDialog;

    public ClueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_clue, container, false);
        ButterKnife.bind(this, rootView);

        Uri vidUrl = ((HuntActivity) getActivity()).getVideoUrl();

        // Creates buffering dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle("Clue Number 1");
        pDialog.setMessage("Buffering");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        pDialog.show();

        // Creates MediaController and binds it to video
        try {
            MediaController mediacontroller = new MediaController(getActivity());
            mediacontroller.setAnchorView(mClueVideoView);
            mClueVideoView.setMediaController(mediacontroller);
            mClueVideoView.setVideoURI(vidUrl);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        mClueVideoView.requestFocus();
        mClueVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                mClueVideoView.start();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onVideoDoneListener) activity;
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

    public interface onVideoDoneListener {
        void onVideoDone();
    }
}
