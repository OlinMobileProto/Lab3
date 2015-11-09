package com.mobileproto.jovanduy.scavengerhunt;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public VideoFragment videoFragment = new VideoFragment();
    public MainActivityFragment mainactivityfragment = new MainActivityFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transitionToFragment(mainactivityfragment);
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
        switch (item.getItemId()){
            case R.id.main_menu:
                transitionToFragment(mainactivityfragment);
                return true;
            case R.id.videos:
                transitionToFragment(videoFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * transitions to a given fragment
     * @param fragment fragment to transition to
     */
    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }



}
