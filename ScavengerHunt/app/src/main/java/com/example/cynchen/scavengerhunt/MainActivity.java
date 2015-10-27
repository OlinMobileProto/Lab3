package com.example.cynchen.scavengerhunt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Incrementing counter for each clue
    private int counter = 5;
    private VideoFragment playClue;
    private FeedFragment feed;

    public int increment_counter(){
        counter +=1;
        return counter;
    }

    //For the other clues, return just the counter
    public int return_counter(){
        return counter;
    }

    public ArrayList<String> locations_videos = new ArrayList<String>();
    public ArrayList<String> longitudes_list = new ArrayList<String>();
    public ArrayList<String> latitudes_list = new ArrayList<String>();

    //Making Volley Request
    public void volley_data() {
        VolleyRequest handler = new VolleyRequest(getApplicationContext());
        //checks that there is even a string

        handler.getlocations(new searchcallback() {
            @Override
            public void callback(ArrayList<String> videos, ArrayList<String> longitudes, ArrayList<String> latitudes) {
                locations_videos = videos;
                longitudes_list = longitudes;
                latitudes_list = latitudes;
                transitionToFragment(playClue);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initializes the searchGoogle fragment and loads it in the container



        //Log.d("volley", volley_locations.toString());
        FeedFragment feed = new FeedFragment();
        GPSFragment gps = new GPSFragment();
        playClue = new VideoFragment();
        CameraFragment takePicture = new CameraFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Automatically transitions to video fragment on initialization

        volley_data();
    }

    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
//        Log.d("WORKED", locations_videos.toString());
        transaction.commit();
    }


    //Boiler Plate Code
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
