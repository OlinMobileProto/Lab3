package hieunguyen.com.scavengerhunt.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hieunguyen.com.scavengerhunt.R;

public class ClueCompleteFragment extends Fragment {


    public ClueCompleteFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.next_clue_button) Button mNextClueButton;
    @OnClick(R.id.next_clue_button) void onNextClue() {
        Intent intent = new Intent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_clue_complete, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public interface onNextClueListener {
        void onNextClue();
    }

}
