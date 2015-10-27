package hieunguyen.com.scavengerhunt.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hieunguyen.com.scavengerhunt.R;

public class InstructionsFragment extends Fragment {

    private onReadyListener mListener;

    @Bind(R.id.done_button) Button mDoneButton;
    @OnClick(R.id.done_button) void ready() {
        mListener.onReady();
    }

    public InstructionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onReadyListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     Interface to listen for fragment changes - ready for first clue
     **/
    public interface onReadyListener{
        void onReady();
    }

}
