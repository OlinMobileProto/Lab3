package com.example.lwilcox.thehunt;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

/**
 * Main activity: This is the activity that all the fragments are a part of.
 */

public class MainActivity extends AppCompatActivity {

    View decorView;
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the status bar
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Changing fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        IntroFragment introFragment = new IntroFragment();
        ft.replace(R.id.container, introFragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hide the status bar.
        decorView.setSystemUiVisibility(uiOptions);
    }
}
