package com.mobileproto.jovanduy.scavengerhunt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Fragment shown after your picture has been uploaded, before transitioning back to watch next video
 */
public class SectionEnd extends Fragment {


    public SectionEnd() {
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
        View rootView = inflater.inflate(R.layout.fragment_section__end, container, false);
        createWebview(rootView);
        createButton(rootView);
        return rootView;
    }

    /**
     * Display WebView with a cat pic saying great job
     * @param view
     */
    public  void createWebview(View view){
        WebView mWebView;
        mWebView = (WebView)view.findViewById(R.id.SectEnd_webView);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        String link ="http://memecrunch.com/meme/QD5D/great-job/image.png";
        mWebView.loadUrl(link);
    }

    /**
     * create the move to next video button. Upon pressing the button, update HuntProgess currState and stageFinal to be on next stage
     * @param view
     */
    public void createButton(View view) {
        Button button = (Button) view.findViewById(R.id.next_video_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.videoFragment.getHuntProgress().updateStageFinal(1);
                mainActivity.videoFragment.getHuntProgress().updateCurrStage(1);
                mainActivity.transitionToFragment(mainActivity.videoFragment);
            }
        });
    }

}
