package hieunguyen.com.scavengerhunt.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hieunguyen.com.scavengerhunt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionsFragment extends Fragment {

    public InstructionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructions, container, false);
    }


    public interface onReadyListener{
        public void onReady();
    }

}
