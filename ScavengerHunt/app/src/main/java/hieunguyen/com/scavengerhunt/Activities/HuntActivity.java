package hieunguyen.com.scavengerhunt.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hieunguyen.com.scavengerhunt.Fragments.ClueFragment;
import hieunguyen.com.scavengerhunt.R;

public class HuntActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ClueFragment())
                    .commit();
        }
    }
}
