package com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.example.yhuang.scavengerhunt.R;

/**
 * Created by yhuang on 10/19/2015.
 */
public class PreviousListener implements View.OnClickListener {

    private Activity m_activity;

    public PreviousListener (Activity activity) {
        m_activity = activity;
    }

    @Override public void onClick(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
        alertDialogBuilder.setMessage(R.string.clue_switch_message);

        //when a positive button is chosen, go to the next clue
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //prev()
            }
        });

        //when a negative button is chosen, go back and do nothing
        alertDialogBuilder.setNegativeButton(R.string.go_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing
            }
        });

        //show the alert
        alertDialogBuilder.show();
    }
}
