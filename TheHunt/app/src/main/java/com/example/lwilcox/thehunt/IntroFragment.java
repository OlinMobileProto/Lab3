package com.example.lwilcox.thehunt;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//TODO: was is this class in the thing below
/**
 * Created by lwilcox on 10/3/2015.
 */
public class IntroFragment extends Fragment {
    private View myFragmentView;
    //TODO: add the Begin/Restart portion of IntroFragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragment_intro, container, false);

        TextView title = (TextView) myFragmentView.findViewById(R.id.title); //TODO: probably don't need these
        TextView description = (TextView) myFragmentView.findViewById(R.id.description);
        Button begin = (Button) myFragmentView.findViewById(R.id.start);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                VideoFragment videoFragment = new VideoFragment();
                ft.replace(R.id.container, videoFragment).addToBackStack("video");
                ft.commit();
            }
        });

        return myFragmentView;
    }
}
