package hieunguyen.com.scavengerhunt.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hieunguyen.com.scavengerhunt.Activities.PhotoActivity;
import hieunguyen.com.scavengerhunt.R;

public class ClueCompleteFragment extends Fragment {

    private onNextClueListener mListener;

    public ClueCompleteFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.next_clue_button) ImageButton mNextClueButton;
    @OnClick(R.id.next_clue_button) void onNextClue() {
        mListener.onNextClue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_clue_complete, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PhotoActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextClueListener");
        }
    }

    /**
     Interface to listen for fragment changes - opening camera
     **/
    public interface onNextClueListener {
        void onNextClue();
    }
}
