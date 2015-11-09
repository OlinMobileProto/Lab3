package com.mobileproto.jovanduy.scavengerhunt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private VideoFragment videoFragment;
    private String TAG = "main_activity_fragment";
    private Button startButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        createButton(rootView, "start_button");
        createButton(rootView, "guide_button");
        createWebview(rootView);
        return rootView;

    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();//TODO: change the import
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (fragment == videoFragment) {
            transaction.addToBackStack(videoFragment.toString());
        }
        transaction.commit();
    }

    public void createButton(View v, String button) {
        if (button.equals("start_button")) {
            Log.d(TAG, "startButton");
            startButton = (Button) v.findViewById(R.id.start_button);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    VideoFragment videoFragment = new VideoFragment();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    VideoFragment videoFragment = mainActivity.videoFragment;
                    videoFragment.getHuntProgress().reset();
                    transitionToFragment(videoFragment);
                }
            });
        }
        else if (button.equals("guide_button")) {
            Log.d(TAG, "guide_button");
            Button guide_button;
            guide_button = (Button) v.findViewById(R.id.guide_button);
            guide_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), R.string.instruction, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public  void createWebview(View view){
        WebView mWebView;
        mWebView = (WebView)view.findViewById(R.id.start_webview);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        String link ="http://scavengerhuntfilm.com/wp-content/themes/scavenger-hunt/img/logo.jpg";
        mWebView.loadUrl(link);
    }
    public Button returnStartButton() {
        return startButton;
    }
}
