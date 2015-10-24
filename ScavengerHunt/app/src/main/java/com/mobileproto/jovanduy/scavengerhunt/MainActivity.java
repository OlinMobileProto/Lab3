    package com.mobileproto.jovanduy.scavengerhunt;

    import android.support.v7.app.AppCompatActivity;
    import android.support.v4.app.Fragment;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public HuntProgress huntProgress = new HuntProgress();
    public VideoFragment videoFragment = new VideoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.huntProgress = huntProgress;
        this.videoFragment = videoFragment;
        MainActivityFragment mainactivityfragment = new MainActivityFragment();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void transitionToFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }



}
