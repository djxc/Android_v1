package com.dj.djArcMap;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.example.administrator.mymap.R;

/**
 * Created by Administrator on 2018/5/4.
 */

public class changeBaseMap extends AppCompatActivity{
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private String[] mNavigationDrawerItemTitles;

    public void setmMap(ArcGISMap mMap) {
        this.mMap = mMap;
    }

    private ArcGISMap mMap;
    private ActionBar actionBar;
    private Activity activity;

    public changeBaseMap(Activity activity, ActionBar actionBar, DrawerLayout mDrawerLayout,
                         ListView mDrawerList, String mActivityTitle, String[] mNavigationDrawerItemTitles){
        this.actionBar = actionBar;
        this.activity = activity;
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerList = mDrawerList;
        this.mActivityTitle = mActivityTitle;
        this.mNavigationDrawerItemTitles = mNavigationDrawerItemTitles;

//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeButtonEnabled(true);
//            // set opening basemap title to Topographic
//            actionBar.setTitle(mNavigationDrawerItemTitles[2]);
//        }
    }
    /**
     * Add navigation drawer items
     */
    public void addDrawerItems() {
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,
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
    public void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
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
            actionBar.setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 1) {
            // position 1 = Navigation Vector
            mMap.setBasemap(Basemap.createNavigationVector());
            actionBar.setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 2) {
            // position 2 = Topographic
            mMap.setBasemap(Basemap.createTopographic());
            actionBar.setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 3) {
            // position 3 = Topographic Vector
            mMap.setBasemap(Basemap.createTopographicVector());
            actionBar.setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 4) {
            // position 3 = Gray Canvas
            mMap.setBasemap(Basemap.createLightGrayCanvas());
            actionBar.setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 5) {
            // position 3 = Gray Canvas Vector
            mMap.setBasemap(Basemap.createLightGrayCanvasVector());
            actionBar.setTitle(mNavigationDrawerItemTitles[position]);
        }
    }


}
