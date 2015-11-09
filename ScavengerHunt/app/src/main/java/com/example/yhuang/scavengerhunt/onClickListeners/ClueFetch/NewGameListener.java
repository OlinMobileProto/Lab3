package com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.yhuang.scavengerhunt.Fragments.StartPage;
import com.example.yhuang.scavengerhunt.MainActivity;

/**
 * OnClick Listener for New Game Button
 */
public class NewGameListener implements View.OnClickListener{

    private Activity m_activity;

    public NewGameListener (Activity activity) {
        m_activity = activity;
    }

    @Override public void onClick(View v) {
        ((MainActivity)m_activity).changeToClue();
    }

}
