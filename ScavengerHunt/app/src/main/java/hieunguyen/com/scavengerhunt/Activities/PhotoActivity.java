package hieunguyen.com.scavengerhunt.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hieunguyen.com.scavengerhunt.Fragments.WelcomeFragment;
import hieunguyen.com.scavengerhunt.R;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new WelcomeFragment())
                    .commit();
        }
    }

}
