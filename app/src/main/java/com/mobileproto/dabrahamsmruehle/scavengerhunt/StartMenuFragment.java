package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartMenuFragment extends Fragment
{

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.continue_button) Button continueButton;
    @Bind(R.id.new_hunt_button) Button newHuntButton;
    Button settingsButton;
    Button helpButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartMenuFragment newInstance()
    {
        StartMenuFragment fragment = new StartMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StartMenuFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_start_menu, container, false);
        ButterKnife.bind(this, view);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((StartMenuFragment.OnFragmentInteractionListener) getActivity())
                        .onFragmentInteraction("continue_button");
            }
        });
        newHuntButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //TODO: Alert dialog, to keep you from accidentally resetting your hunt thing.
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefsEd = sharedPrefs.edit();
                sharedPrefsEd.putInt("current_step", 1);
                ((StartMenuFragment.OnFragmentInteractionListener) getActivity())
                        .onFragmentInteraction("continue_button");
            }
        });
        return view;
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String buttonName);
    }
}
