package com.mobileproto.dabrahamsmruehle.scavengerhunt;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HUDFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HUDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HUDFragment extends Fragment
{

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.curr_clue_button) Button play_current_clue;
    @Bind(R.id.mapview) MapView mapView;
    GpsHandler gpsHandler;
    GoogleMap map;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HUDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HUDFragment newInstance()
    {
        HUDFragment fragment = new HUDFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HUDFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hud, container, false);
        ButterKnife.bind(this, view);
        play_current_clue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((HUDFragment.OnFragmentInteractionListener) getActivity())
                        .onFragmentInteraction(Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.3gp"));

            }
        });

        int checkGooglePlayServices =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(
                    checkGooglePlayServices, getActivity(), 1122).show();
        }

        mapView.onCreate(savedInstanceState);

        // TODO: possibly this should be getMapAsync. right now it is null.
        map = mapView.getMap();

        // TODO: THESE LINES DO NOT WORK RIGHT NOW, BUT WE NEED THEM TO INTERACT WITH THE MAP ITSELF
        // TODO: I THINK.

        // map.getUiSettings().setMyLocationButtonEnabled(true);
        // map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        return view;
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

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
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
        public void onFragmentInteraction(Uri u);
    }

}
