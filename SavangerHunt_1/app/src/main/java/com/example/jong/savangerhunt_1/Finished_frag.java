package com.example.jong.savangerhunt_1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Finished_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Finished_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Finished_frag extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static Finished_frag newInstance(String param1, String param2) {
        Finished_frag fragment = new Finished_frag();
        return fragment;
    }

    public Finished_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished_frag, container, false);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
