package hieunguyen.com.scavengerhunt.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import hieunguyen.com.scavengerhunt.R;

public class CameraFragment extends Fragment {

    private OnFragmentChangeListener mListener;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.take_photo_button) Button mPhotoButton;
    @OnClick(R.id.take_photo_button) void onPhotoButtonClicked() {
        mListener.onCameraButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentChangeListener) activity;
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

    public interface OnFragmentChangeListener {
        public void onCameraButton();
    }

}
