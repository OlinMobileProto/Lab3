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
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hieunguyen.com.scavengerhunt.R;

public class ClueFragment extends Fragment {

//    private onVideoDoneListener mListener;

    @Bind(R.id.done_button) Button mDoneButton;
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

//        Uri vidUrl = Uri.parse("http://s3.amazonaws.com/olin-mobile-proto/MVI_3140.MOV");

        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        Uri vidUrl = Uri.parse(vidAddress);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle("Clue Number 1");
        pDialog.setMessage("Buffering");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        pDialog.show();

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

        //TODO: Figure out the best way to get the current clue
        //TODO: Figure out how to display a video
        //TODO: Figure out how to determine when the video has been played

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

//    public interface onVideoDoneListener {
//        public void onVideoDone();
//    }

}
