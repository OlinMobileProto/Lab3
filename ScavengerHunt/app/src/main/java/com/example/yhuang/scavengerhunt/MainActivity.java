package com.example.yhuang.scavengerhunt;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.yhuang.scavengerhunt.Fragments.ClueFetch;
import com.example.yhuang.scavengerhunt.Database.CallbackInterface;
import com.example.yhuang.scavengerhunt.Database.ClueDBConnection;
import com.example.yhuang.scavengerhunt.Database.ClueRow;
import com.example.yhuang.scavengerhunt.Fragments.StartPage;

import java.util.Map;

/**
 * In MainActivity, we set up the online
 * database connection, fragment transaction
 * manager, as well as information returned
 * from database.
 *
 * As the MainActivity finished initialization,
 * it uses fragment transaction manager to
 * switch to StartPage fragment
 */

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm = (FragmentManager) getSupportFragmentManager();
    public static Map<Integer,ClueRow.Row> locationMap; //Map to access all info about a clue, including s3video link and set location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClueDBConnection clueLocations = new ClueDBConnection(this); // setup database connection

        // this callback function needs to be moved out to another class
        clueLocations.getLocations(new CallbackInterface() {
            @Override
            public void resultsCallback(Map<Integer, ClueRow.Row> locationArray) {
                // need to add an error handler for network issue
                locationMap = locationArray;
            }
        });

        //begin fragment transaction to ClueFetch
        //Checks the string that intent passes
        Intent intent = this.getIntent();
        String strdata = this.getIntent().getStringExtra("Class");
        //Depending on the string, change to the specific fragment
        if (strdata != null) {
            System.out.println(intent);
            if (strdata.equals("CameraActivity")) {
                changeToClue();
            } else {
                changeToMain();
            }
        }else {
            changeToMain();// should first change to the start page fragment
        }
    }

    public void changeToMain() {
        //Chenge to the start page
        StartPage start_fragment = new StartPage();
        transitionToFragment(start_fragment);
    }

    //change to ClueFetch fragment
    public void changeToClue() {
        ClueFetch clue_fragment = new ClueFetch();
        transitionToFragment(clue_fragment);
    }

    //general fragment transition method
    private void transitionToFragment(Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_activity, fragment);
        transaction.commit();
    }
}
