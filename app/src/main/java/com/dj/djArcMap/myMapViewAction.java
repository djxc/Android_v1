package com.dj.djArcMap;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.administrator.mymap.R;

import java.util.Iterator;

/**
 * Created by Administrator on 2018/5/5.
 */

public class myMapViewAction {
    private MapView mapView;
    private Activity activity;
    private Callout mCallout;
    private TextView calloutContent;
    private boolean canSelect = false;
    private boolean isShowLatLon = false;

    private FeatureLayer featureLayer;
    private ArcGISMap map;

    public myMapViewAction(MapView mapView, Activity activity, TextView calloutContent, ArcGISMap map){
        this.mapView = mapView;
        this.activity = activity;
        this.calloutContent = calloutContent;
        this.map = map;
        mapView.setOnTouchListener(onTouchListener());
    }

    /**
     * 显示经纬度的开关
     */
    public void edit_showLatLon(){
        if(isShowLatLon){
            isShowLatLon = false;
            mCallout.dismiss();
        }else {
            isShowLatLon = true;
        }
    }

    /**
     * 可选与不可选的开关
     */
    public void edit_canSelect(){
        if(canSelect){
            canSelect = false;
            featureLayer.clearSelection();
        }else{
            canSelect = true;
        }
    }

    /**
     * MapView 的触摸事件，这里有：
     * 1。显示经纬度
     * 2. 选择要素
     * @return
     */
    private DefaultMapViewOnTouchListener onTouchListener(){
        DefaultMapViewOnTouchListener mylistener = new DefaultMapViewOnTouchListener(activity, mapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(isShowLatLon){
                    Log.i("dj", "onSingleTapConfirmed: " + e.toString());

                    // get the point that was clicked and convert it to a point in map coordinates
                    android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()),
                            Math.round(e.getY()));
                    // create a map point from screen point
                    Point mapPoint = mapView.screenToLocation(screenPoint);
                    // convert to WGS84 for lat/lon format
                    Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
                    // create a textview for the callout
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine();
                    // format coordinates to 4 decimal places
                    calloutContent.setText("Lat: " + String.format("%.4f", wgs84Point.getY()) +
                            ", Lon: " + String.format("%.4f", wgs84Point.getX()));

                    // get callout, set content and show
                    mCallout = mapView.getCallout();
                    mCallout.setLocation(mapPoint);
                    mCallout.setContent(calloutContent);
                    mCallout.show();

                    mapView.setViewpointCenterAsync(mapPoint);  //移动到以该点为中心
                    return true;
                }
                if(canSelect){
//                     get the point that was clicked and convert it to a point in map coordinates
                    Point clickPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
                    int tolerance = 10;
                    double mapTolerance = tolerance * mMapView.getUnitsPerDensityIndependentPixel();
                    // create objects required to do a selection with a query
                    Envelope envelope = new Envelope(clickPoint.getX() - mapTolerance, clickPoint.getY() - mapTolerance, clickPoint.getX() + mapTolerance, clickPoint.getY() + mapTolerance, map.getSpatialReference());
                    QueryParameters query = new QueryParameters();
                    query.setGeometry(envelope);
                    // call select features
                    final ListenableFuture<FeatureQueryResult> future = featureLayer.selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW);
                    // add done loading listener to fire when the selection returns
                    future.addDoneListener(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //call get on the future to get the result
                                FeatureQueryResult result = future.get();
                                // create an Iterator
                                Iterator<Feature> iterator = result.iterator();
                                Feature feature;
                                // cycle through selections
                                int counter = 0;
                                while (iterator.hasNext()){
                                    feature = iterator.next();
                                    counter++;
                                    Log.i("dj", "Selection #: " + counter + " Table name: " + feature.getFeatureTable().getTableName());
                                }
                                Toast.makeText(activity, counter + " features selected", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("DJ", "Select feature failed: " + e.getMessage());
                            }
                        }
                    });
                    return super.onSingleTapConfirmed(e);
                }
                return true;
            }
        };
        return mylistener;
    }

    public void setFeatureLayer(FeatureLayer featureLayer) {
        this.featureLayer = featureLayer;
    }

}
