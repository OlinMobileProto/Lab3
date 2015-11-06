package com.example.yhuang.scavengerhunt.onClickListeners.ClueFetch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;
import com.example.yhuang.scavengerhunt.CameraActivity;
import com.example.yhuang.scavengerhunt.Fragments.GpsDetection;
import com.example.yhuang.scavengerhunt.MainActivity;
import com.example.yhuang.scavengerhunt.R;

/**
 * When the camera button is hit, the
 * CameraListener should perform the following
 * tasks:
 *
 * 1. check whether user has reached the location
 *
 * 2. if true, check with the package manager
 * to see if camera device is available. If
 * true, call the camera Activity.
 *
 * 3.if the user has not get the location,
 * pops up an alertDialog to inform user
 * and provides hint if necessary.
 */

public class CameraListener implements View.OnClickListener {

    private GpsDetection m_gpsInfo;
    private Activity m_activity;
    private PackageManager m_packageManager;
    //private Map<Integer,ClueRow.Row> locationMap = MainActivity.locationMap;
    private int m_curClueNum;

    public CameraListener (GpsDetection gpsInfo, Activity activity, PackageManager packageManager, int curClueNum) {
        m_gpsInfo = gpsInfo;
        m_activity = activity;
        m_packageManager = packageManager;
        m_curClueNum = curClueNum;
    }

    @Override public void onClick(View v) {

        Boolean m_getSpot = m_gpsInfo.canGetLocation(m_gpsInfo.latitudeInfo(),m_gpsInfo.longitudeInfo(),
                MainActivity.locationMap.get(m_curClueNum).lat, MainActivity.locationMap.get(m_curClueNum).lon); //update user's GPS status

        //Using PackageManager to check if an Android device has a camera from within a fragment
        if(!m_packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //print into string.xml
            Toast.makeText(m_activity, "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //if m_getSpot is true, open the camera if not, ask if the user want some hints
        if (m_getSpot) {
            //send intent to CameraActivity
            Intent intent = new Intent(m_activity, CameraActivity.class);
            intent.putExtra("curClueNum", m_curClueNum);
            m_activity.startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
            alertDialogBuilder.setMessage(R.string.hint_message);

            //when a positive button is hit, give the hint
            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String clue = Double.toString(m_gpsInfo.calculateDistance(m_gpsInfo.latitudeInfo(),
                            m_gpsInfo.longitudeInfo(), MainActivity.locationMap.get(m_curClueNum).lat, MainActivity.locationMap.get(m_curClueNum).lon))
                            + " " + m_gpsInfo.calculateDirection(m_gpsInfo.latitudeInfo(),m_gpsInfo.longitudeInfo(),
                            MainActivity.locationMap.get(m_curClueNum).lat, MainActivity.locationMap.get(m_curClueNum).lon);

                    Toast.makeText(m_activity, clue,
                            Toast.LENGTH_LONG).show();
                }
            });

            //when a negative button is hit, do nothing and go back
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

}
