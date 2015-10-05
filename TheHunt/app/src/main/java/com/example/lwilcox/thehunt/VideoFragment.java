package com.example.lwilcox.thehunt;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Context;

/**
 * Created by lwilcox on 10/3/2015.
 */
public class VideoFragment extends Fragment {
    private View myFragmentView;
    public ImageView img1;
    public ImageView img2;
    public ImageView img3;
    public ImageView img4;
    public ImageView img5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_video, container, false);

        img1 = (ImageView) getActivity().findViewById(R.id.imageView1);
        img2 = (ImageView) getActivity().findViewById(R.id.imageView1);
        img3 = (ImageView) getActivity().findViewById(R.id.imageView1);
        img4 = (ImageView) getActivity().findViewById(R.id.imageView1);
        img5 = (ImageView) getActivity().findViewById(R.id.imageView1);
        Drawable myDrawable = getActivity().getResources().getDrawable(R.drawable.cameraimage);

        img1.setImageDrawable(myDrawable);
        img2.setImageDrawable(myDrawable);
        img3.setImageDrawable(myDrawable);
        img4.setImageDrawable(myDrawable);
        img5.setImageDrawable(myDrawable);
        return myFragmentView;
    }
}
