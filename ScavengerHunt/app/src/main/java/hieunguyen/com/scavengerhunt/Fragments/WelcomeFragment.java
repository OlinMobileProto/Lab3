package hieunguyen.com.scavengerhunt.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hieunguyen.com.scavengerhunt.Data.DbService;
import hieunguyen.com.scavengerhunt.R;

public class WelcomeFragment extends Fragment {

    DbService dbService;
    @Bind(R.id.goButton) ImageButton mGoButton;
    private onGoListener mListener;

    @OnClick(R.id.goButton) void go() {
        mListener.onGo();
    }

    public WelcomeFragment() {
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

        dbService = new DbService(getActivity());
        if (dbService.isDbEmpty()) {
            dbService.update();
        } else {
            dbService.changeActiveClue(-1, 3);
            for (int i=1; i<7; i++) {
                Log.d("WELCOME", dbService.getClue(i).toString());
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onGoListener) activity;
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


    public interface onGoListener {
        void onGo();
    }
}
