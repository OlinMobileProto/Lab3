package hieunguyen.com.scavengerhunt.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import hieunguyen.com.scavengerhunt.Fragments.InstructionsFragment;
import hieunguyen.com.scavengerhunt.Fragments.WelcomeFragment;
import hieunguyen.com.scavengerhunt.R;

public class StartActivity extends Activity implements WelcomeFragment.onGoListener, InstructionsFragment.onReadyListener{

    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new WelcomeFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
    Overrides onGo() method of WelcomeFragment.onGoListener
     **/
    @Override
    public void onGo() {
        // Replace WelcomeFragment with InstructionsFragment
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new InstructionsFragment())
                .commit();
    }

    /**
    Overrides onReady() method of InstructionsFragment.onReadyListener
     **/
    @Override
    public void onReady() {
        // Launch HuntActivity
        Intent intent = new Intent(this, HuntActivity.class);
        startActivity(intent);
    }
}
