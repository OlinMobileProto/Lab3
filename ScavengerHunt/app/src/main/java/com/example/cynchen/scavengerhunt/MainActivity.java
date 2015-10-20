package com.example.cynchen.scavengerhunt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    //Incrementing counter for each clue
    int counter = 0;

    public int increment_counter(){
        counter +=1;
        return counter;
    }

    //For the other clues
    public int return_counter(){
        return counter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initializes the searchGoogle fragment and loads it in the container
        GPSFragment gps = new GPSFragment();
        VideoFragment playClue = new VideoFragment();
        CameraFragment takePicture = new CameraFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transitionToFragment(playClue);
    }

    public void transitionToFragment(Fragment fragment){
        //This function takes as input a fragment, initializes the fragment manager and replaces
        //the container with the provided fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


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
