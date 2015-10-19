package hieunguyen.com.scavengerhunt.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hieunguyen.com.scavengerhunt.R;

public class ClueCompleteFragment extends Fragment {


    public ClueCompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upload_photo, container, false);

        return rootView;
    }
}
