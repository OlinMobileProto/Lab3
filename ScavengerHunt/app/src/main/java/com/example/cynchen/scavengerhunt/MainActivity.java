package com.example.cynchen.scavengerhunt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cynchen.scavengerhunt.HomeScreen.HomeScreenFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //The homescreenfragment gets called first
    private HomeScreenFragment homescreen = new HomeScreenFragment();

    //Incrementing counter for each clue
    private int counter = 0;

    public int increment_counter(){
        counter +=1;
        return counter;
    }

    //For the other clues, return just the counter number for GPS, camera
    public int return_counter(){
        return counter;
    }

    public ArrayList<String> locations_videos = new ArrayList<String>();
    public ArrayList<String> longitudes_list = new ArrayList<String>();
    public ArrayList<String> latitudes_list = new ArrayList<String>();

    //Making Volley Request
    public void volley_data() {
        VolleyRequest handler = new VolleyRequest(getApplicationContext());

        //fills the arraylist with the outcome of the volley request
        handler.getlocations(new searchcallback() {
            @Override
            public void callback(ArrayList<String> videos, ArrayList<String> longitudes, ArrayList<String> latitudes) {
                locations_videos = videos;
                longitudes_list = longitudes;
                latitudes_list = latitudes;

                //transition to the fragment homescreen first
                transitionToFragment(homescreen);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calls volley and transitions to fragment of homescreen
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
