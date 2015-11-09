package com.example.jong.savangerhunt_1;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,notifystagefinished {
    private int currstage;
    private DrawerLayout mDrawerlayout;
    private ListView mDrawerlist;
    private ActionBarDrawerToggle toggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private FragmentManager fm;
    private FrameLayout frame;
    private StageData stageData;
    private ArrayList<String> Stage_list;
    private ArrayAdapter<String> mArrayadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stageData=new StageData(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();


        //TODO:rename the variable
        currstage = stageData.getCurrstage();
        Stage_list= new ArrayList<String>();
        for (int i=0; i<currstage; i++) {
            Stage_list.add("Stage ".concat(Integer.toString(i+1)));
        }


        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerTitle=mTitle=getTitle();
        mDrawerlist = (ListView) findViewById(R.id.nav_listview);
        mArrayadapter=new ArrayAdapter<String>(this, R.layout.drawerlistitem, Stage_list);
        mDrawerlist.setAdapter(mArrayadapter);
        mDrawerlist.setOnItemClickListener(new DrawerItemClickListener());
        frame = (FrameLayout) findViewById(R.id.container_frame);
        fm.beginTransaction()
                .replace(R.id.container_frame, new ViewpagerContainer())
                .commit();


        toggle = new ActionBarDrawerToggle(
                this, mDrawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerview){
                super.onDrawerOpened(drawerview);
                getSupportActionBar().setTitle(mDrawerTitle);
                mDrawerlayout.bringChildToFront(drawerview);
                mDrawerlayout.requestLayout();
                invalidateOptionsMenu();
            }

            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                float moveFactor = (mDrawerlist.getWidth() * slideOffset);
                frame.setTranslationX(moveFactor);
            }
        };

        mDrawerlayout.setDrawerListener(toggle);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            stageData.changevisiblestage(position+1);
            ViewpagerContainer container=(ViewpagerContainer) getSupportFragmentManager().findFragmentById(R.id.container_frame);
            TabbedFragment_clip clip= (TabbedFragment_clip) container.getviewpagerfragment(0);
            TabbedFragment_map map = (TabbedFragment_map) container.getviewpagerfragment(1);
            clip.updateclipview(position+1);
            map.updatemapview(position+1);
        }
    }
    @Override
    public void updatemainview(int currstage){
        Stage_list.add("Stage ".concat(Integer.toString(currstage)));
        mArrayadapter.notifyDataSetChanged();

    }
}
