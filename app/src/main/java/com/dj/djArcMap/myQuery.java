package com.dj.djArcMap;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureCollection;
import com.esri.arcgisruntime.data.FeatureCollectionTable;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureCollectionLayer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.example.administrator.mymap.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by 杜杰 on 2018/4/20.
 */

public class myQuery implements View.OnClickListener{
    private String url;
    private MapView mapView;
    private ListenableFuture<FeatureQueryResult> queryResult;

    public myQuery(MapView mapView, String url){
        this.mapView = mapView;
        this.url = url;
    }

    /**
     * 接受查询语句进行查询
     * @param queryStr
     */
    public void executeQuery(String queryStr){
        //initialize service feature table to be queried
        FeatureTable featureTable = new ServiceFeatureTable(url);

        //create query parameters
        QueryParameters queryParams = new QueryParameters();

        // 1=1 will give all the features from the table
        queryParams.setWhereClause(queryStr);

        //query feature from the table
        queryResult = featureTable.queryFeaturesAsync(queryParams);
        queryResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                showResult();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    /**
     * 显示查询结果
     */
    private void showResult(){
        try {
            SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.TRIANGLE, Color.BLUE, 5);
            SimpleRenderer simpleRenderer = new SimpleRenderer(symbol);
            //create a feature collection table from the query results
            FeatureCollectionTable featureCollectionTable = new FeatureCollectionTable(queryResult.get());
            featureCollectionTable.setRenderer(simpleRenderer);
            //create a feature collection from the above feature collection table
            FeatureCollection featureCollection = new FeatureCollection();
            featureCollection.getTables().add(featureCollectionTable);

            //create a feature collection layer
            FeatureCollectionLayer featureCollectionLayer = new FeatureCollectionLayer(featureCollection);

            //add the layer to the operational layers array
            mapView.getMap().getOperationalLayers().add(featureCollectionLayer);
        } catch (InterruptedException | ExecutionException e) {
            Log.i("dj", "Error in FeatureQueryResult: " + e.getMessage());
        }
    }


}
