package com.example.yhuang.scavengerhunt;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.yhuang.scavengerhunt.Fragments.ClueFetch;
import com.example.yhuang.scavengerhunt.Fragments.GpsDetection;
import com.example.yhuang.scavengerhunt.Fragments.ImageUpload;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment transaction for switching between fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ImageUpload image_fragment = new ImageUpload();
        fragmentTransaction.add(R.id.fragment_activity, image_fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    //Change to a clue fragment
    public void changeToClue() {
        FragmentTransaction fTrac = getSupportFragmentManager().beginTransaction();
        ClueFetch clue_fragment = new ClueFetch();
        fTrac.replace(R.id.fragment_activity, clue_fragment);
        fTrac.commit();
    }

    //Change to a GPS fragment
    public void changeToGps() {
        FragmentTransaction fTrac = getSupportFragmentManager().beginTransaction();
        GpsDetection gps_fragment = new GpsDetection();
        fTrac.replace(R.id.fragment_activity, gps_fragment);
        fTrac.commit();
    }
}
