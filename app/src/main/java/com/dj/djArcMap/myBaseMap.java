package com.dj.djArcMap;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.administrator.mymap.R;

public class myBaseMap extends AppCompatActivity {
    private MapView mMapView;
    private ArcGISMap mMap;
    private String[] mNavigationDrawerItemTitles;

    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private Activity activity;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_base_map);
        activity = this;
        actionBar = getSupportActionBar();
        // inflate navigation drawer
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.vector_tiled_types);
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // get app title
        mActivityTitle = getTitle().toString();
//        changeBaseMap changeBMap = new changeBaseMap(activity, actionBar, mDrawerLayout, mDrawerList, mActivityTitle, mNavigationDrawerItemTitles);
//        changeBMap.addDrawerItems();
//        changeBMap.setupDrawer();
        addDrawerItems();
        setupDrawer();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            // set opening basemap title to Topographic
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[2]);
        }

        // inflate MapView from layout
        mMapView = (MapView) findViewById(R.id.mapView);
        // create a map with Topographic Basemap
        mMap = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 47.6047381, -122.3334255, 12);
        // set the map to be displayed in this view
        mMapView.setMap(mMap);

//        changeBMap.setmMap(mMap);

    }

    /**
     * Add navigation drawer items
     */
    private void addDrawerItems() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                mNavigationDrawerItemTitles);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectBasemap(position);
            }
        });
    }

    /**
     * Set up the navigation drawer
     */
    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }


    /**
     * Select the Basemap item based on position in the navigation drawer
     *
     * @param position order int in navigation drawer
     */
    private void selectBasemap(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

        // if-else is used because this sample is used elsewhere as a Library module
        if (position == 0) {
            // position 0 = Streets
            mMap.setBasemap(Basemap.createStreets());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 1) {
            // position 1 = Navigation Vector
            mMap.setBasemap(Basemap.createNavigationVector());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 2) {
            // position 2 = Topographic
            mMap.setBasemap(Basemap.createTopographic());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 3) {
            // position 3 = Topographic Vector
            mMap.setBasemap(Basemap.createTopographicVector());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 4) {
            // position 3 = Gray Canvas
            mMap.setBasemap(Basemap.createLightGrayCanvas());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 5) {
            // position 3 = Gray Canvas Vector
            mMap.setBasemap(Basemap.createLightGrayCanvasVector());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        return (mDrawerToggle.onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }

}
