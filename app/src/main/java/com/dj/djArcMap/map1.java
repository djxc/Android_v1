package com.dj.djArcMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dj.DataOperator.SDCard;
import com.dj.djtest.MyAdapter;
import com.dj.djtest.zhedie;
import com.dj.myMenu.extendMenu;
import com.dj.myMenu.menuAction;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.example.administrator.mymap.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by 杜杰 on 2018/4/18.
 */

public class map1 extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "dj";
    private Activity activity;
    private MapView mapView;
    private Dialog dialog;
    private ArcGISMap map;
    private Geometry geometry;

    private ImageButton mPointButton;
    private ImageButton mMultiPointButton;
    private ImageButton mPolylineButton;
    private ImageButton mPolygonButton;
    private ImageButton mFreehandLineButton;
    private ImageButton mFreehandPolygonButton;
    private Button myToolbar;
    private View editeToolbar;
    private Toolbar mToolbar;
    private MenuView.ItemView stopEdit;

    private StorageManager mStorageManager;
    private Button json2Geomtry;

    private FloatingActionButton floatingActionButton;

    private mySketchEditor msketchEditor;
    private FeatureLayer featureLayer;

    private ActionBar actionBar;
    private ListView mDrawerList;
    private LinearLayout my_drawer;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private String[] mNavigationDrawerItemTitles;

    private myMapViewAction mapViewAction;
    private menuAction myMenu = null;
    private myLayers djLayers = null;

    private ExpandableListView mExpandableListView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymap);
        activity = this;

        //获取上一个intent传递过来的数据
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Toast.makeText(activity, data, Toast.LENGTH_LONG).show();

        djLayers = new myLayers();
        mStorageManager = (StorageManager)getSystemService(Context.STORAGE_SERVICE);
        mapView = (MapView) findViewById(R.id.mapView);
        json2Geomtry = (Button) findViewById(R.id.json2Geomtry);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        map = new ArcGISMap(Basemap.Type.DARK_GRAY_CANVAS_VECTOR, 34.056295, 117.195800, 10);
        mapView.setMap(map);
        showDialog();

        // get buttons from layouts
        mPointButton = (ImageButton) findViewById(R.id.pointButton);
        mMultiPointButton = (ImageButton)findViewById(R.id.pointsButton);
        mPolylineButton = (ImageButton)findViewById(R.id.polylineButton);
        mPolygonButton = (ImageButton)findViewById(R.id.polygonButton);
        mFreehandLineButton = (ImageButton)findViewById(R.id.freehandLineButton);
        mFreehandPolygonButton = (ImageButton)findViewById(R.id.freehandPolygonButton);

        my_drawer = (LinearLayout) findViewById(R.id.my_drawer);

        editeToolbar =  findViewById(R.id.toolbarInclude);
        myToolbar = (Button) findViewById(R.id.mToolbar);   //工具栏按钮
        mToolbar = (Toolbar) findViewById(R.id.toolbar);    //工具栏

        myToolbar.setOnClickListener(this);

        /**
         * 设置地图监听，地图加载完成后触发该事件。新创建一个线程
         */
        map.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                DismissDialog();
            }
        });

        mDrawerList = (ListView) findViewById(R.id.mymap_navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mymap_drawerLayout);
        // get app title
        mActivityTitle = getTitle().toString();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.vector_tiled_types);
        actionBar = getSupportActionBar();
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

        floatingActionButton.setOnClickListener(this);

        json2Geomtry.setOnClickListener(this);

        mapViewAction = new myMapViewAction(mapView, activity, new TextView(getApplicationContext()), map);


        /**
         * 折叠菜单
         */
        // 获取组件
        mExpandableListView = (ExpandableListView) findViewById(R.id.expendablelistview);
        mExpandableListView.setGroupIndicator(null);
        new extendMenu(activity, mExpandableListView, this);


    }

    /**
     * 添加栅格图层
     */
    public void addRaster(){
        djLayers.loadRaster(mapView, map, getString(R.string.raster_path));
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
        closeDrawer();


        // if-else is used because this sample is used elsewhere as a Library module
        if (position == 0) {
            // position 0 = Streets
            map.setBasemap(Basemap.createStreets());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 1) {
            // position 1 = Navigation Vector
            map.setBasemap(Basemap.createNavigationVector());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 2) {
            // position 2 = Topographic
            map.setBasemap(Basemap.createTopographic());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 3) {
            // position 3 = Topographic Vector
            map.setBasemap(Basemap.createTopographicVector());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 4) {
            // position 3 = Gray Canvas
            map.setBasemap(Basemap.createLightGrayCanvas());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        } else if (position == 5) {
            // position 3 = Gray Canvas Vector
            map.setBasemap(Basemap.createLightGrayCanvasVector());
            getSupportActionBar().setTitle(mNavigationDrawerItemTitles[position]);
        }
    }

    public void closeDrawer(){
        mDrawerLayout.closeDrawer(my_drawer);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mToolbar:
                visiableAction();
                break;
            case R.id.fab:
                createGeomtry();
                break;
            case R.id.json2Geomtry:
                createGeomtryFromJson();
                break;
        }
    }

    /**
     * 通过属性表进行查询
     */
    public void QueryTest(){
        String url = getResources().getString(R.string.wildfire_feature_server);
        myQuery query = new myQuery(mapView, url);
        query.executeQuery("1=1");
    }


    /**
     * 显示或隐藏菜单栏
     */
    private void in_visibleToolbar(){
        if(mToolbar.getVisibility()==View.GONE){
            mToolbar.setVisibility(View.VISIBLE);
        }else{
            mToolbar.setVisibility(View.GONE);
        }
    }

    private void visiableAction(){
        if(actionBar.isShowing()){
            actionBar.hide();
        }else {
            actionBar.show();
        }
    }

    /**
     * 开始编辑，绘制草图
     */
    public void startEdit(){
        if(editeToolbar.getVisibility()== View.GONE){
            editeToolbar.setVisibility(View.VISIBLE);
            msketchEditor = new mySketchEditor(mapView, editeToolbar, myToolbar, mToolbar);
            msketchEditor.setButtons(mPointButton, mMultiPointButton, mPolylineButton, mPolygonButton, mFreehandLineButton, mFreehandPolygonButton);
        }else{
            editeToolbar.setVisibility(View.GONE);
            msketchEditor = null;
        }
    }

    /**
     * 创建菜单按钮，绑定菜单的xml文件
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        myMenu = new menuAction(menu, inflater, msketchEditor,this, mapViewAction);
        myMenu.createMenu();
        return super.onCreateOptionsMenu(menu);

    }

    /**
     *   监听菜单按钮事件，点击不同的按钮作出不同的反映
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myMenu.menuItemSelected(item)){
            return true;
        }else {
            return (mDrawerToggle.onOptionsItemSelected(item))||super.onOptionsItemSelected(item);
        }
    }

    public void addFeatureLayer(){
        djLayers.featureLayerShapefile(mapView, activity, getString(R.string.shapefile_path));
        getFeatureLayer();
    }

    private void getFeatureLayer(){
        featureLayer = (FeatureLayer) mapView.getMap().getOperationalLayers().get(0);
        mapViewAction.setFeatureLayer(featureLayer);
    }

    /**
     * 通过json文件生成geometry
     */
    private void createGeomtryFromJson(){
        SDCard sdCard = new SDCard(activity);
        String json = "";
        String path = sdCard.getStorages(mStorageManager).get(0).toString();   //获取手机内存位置
        try{
            FileInputStream inputStream = new FileInputStream(new File(path+"/djData/data.json"));
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            json = new String(b);
        }catch (Exception e){
            Log.i("dj",e.toString());
        }
        geometry = Geometry.fromJson(json);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.CROSS, Color.BLUE, null);
        GraphicsOverlay overlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(overlay);
        overlay.getGraphics().add(new Graphic(geometry, fillSymbol));
    }

    /**
     * 创建geometry，定义了symbol样式在图层中显示几何图形
     */
    private void createGeomtry(){
        // create color and symbols for drawing graphics
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.TRIANGLE, Color.BLUE, 14);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.CROSS, Color.BLUE, null);
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3);

        // add a graphic of point, multipoint, polyline and polygon.
        GraphicsOverlay overlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(overlay);
        overlay.getGraphics().add(new Graphic(createPolygon(), fillSymbol));
        overlay.getGraphics().add(new Graphic(createPolyline(), lineSymbol));
        overlay.getGraphics().add(new Graphic(createMultipoint(), markerSymbol));
        overlay.getGraphics().add(new Graphic(createPoint(), markerSymbol));

        // use the envelope to set the map viewpoint
        mapView.setViewpointGeometryAsync(createEnvelope(), getResources().getDimension(R.dimen.viewpoint_padding));
    }

    private Envelope createEnvelope() {

        //[DocRef: Name=Create Envelope, Category=Fundamentals, Topic=Geometries]
        // create an Envelope using minimum and maximum x,y coordinates and a SpatialReference
        Envelope envelope = new Envelope(-123.0, 23.5, -101.0, 68.0, SpatialReferences.getWgs84());
        //[DocRef: END]

        return envelope;
    }

    //{"x":34.056294999999999,"y":-117.19580000000001,"spatialReference":{"wkid":4326}}
    private Point createPoint() {
        //[DocRef: Name=Create Point, Category=Fundamentals, Topic=Geometries]
        // create a Point using x,y coordinates and a SpatialReference
        Point pt = new Point(34.056295, -117.195800, SpatialReferences.getWgs84());
        //[DocRef: END]

        return pt;
    }

    //{"points":[[-121.49101400000001,38.579065],[-122.891366,47.039231000000001],[-123.043814,44.933259999999997],[-119.766999,39.164884999999998]],"spatialReference":{"wkid":4326}}
    private Multipoint createMultipoint() {
        //[DocRef: Name=Create Multipoint, Category=Fundamentals, Topic=Geometries]
        // create a Multipoint from a PointCollection
        PointCollection stateCapitalsPST = new PointCollection(SpatialReferences.getWgs84());
        stateCapitalsPST.add(-121.491014, 38.579065); // Sacramento, CA
        stateCapitalsPST.add(-122.891366, 47.039231); // Olympia, WA
        stateCapitalsPST.add(-123.043814, 44.93326); // Salem, OR
        stateCapitalsPST.add(-119.766999, 39.164885); // Carson City, NV
        Multipoint multipoint = new Multipoint(stateCapitalsPST);
        //[DocRef: END]
        return multipoint;
    }

    //{"paths":[[[-119.992,41.988999999999997],[-119.994,38.994],[-114.62,35]]],"spatialReference":{"wkid":4326}}
    private Polyline createPolyline() {
        //[DocRef: Name=Create Polyline, Category=Fundamentals, Topic=Geometries]
        // create a Polyline from a PointCollection
        PointCollection borderCAtoNV = new PointCollection(SpatialReferences.getWgs84());
        borderCAtoNV.add(-119.992, 41.989);
        borderCAtoNV.add(-119.994, 38.994);
        borderCAtoNV.add(-114.620, 35.0);
        Polyline polyline = new Polyline(borderCAtoNV);
        //[DocRef: END]

        return polyline;
    }

    //{"rings":[[[-109.048,40.997999999999998],[-102.047,40.997999999999998],[-102.03700000000001,36.988999999999997],
    // [-109.048,36.997999999999998],[-109.048,40.997999999999998]]],"spatialReference":{"wkid":4326}}
    private Polygon createPolygon() {
        //[DocRef: Name=Create Polygon, Category=Fundamentals, Topic=Geometries]
        // create a Polygon from a PointCollection
        PointCollection coloradoCorners = new PointCollection(SpatialReferences.getWgs84());
        coloradoCorners.add(-109.048, 40.998);
        coloradoCorners.add(-102.047, 40.998);
        coloradoCorners.add(-102.037, 36.989);
        coloradoCorners.add(-109.048, 36.998);
        Polygon polygon = new Polygon(coloradoCorners);
        //[DocRef: END]
        return polygon;
    }

    /**
     * 定以加载动画
     */
    private void showDialog(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.mydialog);
        dialog.show();
    }

    /**
     * 加载地图显示加载动画，当地图加载完成后就消失
     */
    private void DismissDialog(){
        if (map.getLoadStatus() == LoadStatus.LOADED) {
            Toast.makeText(activity, "加载成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.dispose();
    }

}

