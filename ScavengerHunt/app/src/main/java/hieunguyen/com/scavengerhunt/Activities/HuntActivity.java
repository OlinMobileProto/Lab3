package hieunguyen.com.scavengerhunt.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hieunguyen.com.scavengerhunt.Fragments.ClueFragment;
import hieunguyen.com.scavengerhunt.Fragments.MapFragment;
import hieunguyen.com.scavengerhunt.R;

public class HuntActivity extends AppCompatActivity implements ClueFragment.onVideoDoneListener {

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

    public Uri getVideoUrl(int clueNumber){
        return Uri.parse("https://s3.amazonaws.com/olin-mobile-proto/MVI_3140.MOV");
    }

    @Override
    public void onVideoDone() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new MapFragment())
                .commit();
    }
}
