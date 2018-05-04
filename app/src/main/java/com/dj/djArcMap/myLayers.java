package com.dj.djArcMap;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import static com.dj.djArcMap.map1.TAG;

/**
 * Created by 杜杰 on 2018/4/25.
 */

public class myLayers {

    /**
     * Creates a ShapefileFeatureTable from a service and, on loading, creates a FeatureLayer and add it to the map.
     */
    public void featureLayerShapefile(final MapView mMapView, final Activity activity, final String shapefile_path) {
        // load the shapefile with a local path
        final ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(
                Environment.getExternalStorageDirectory() + shapefile_path);

        shapefileFeatureTable.loadAsync();
        shapefileFeatureTable.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (shapefileFeatureTable.getLoadStatus() == LoadStatus.LOADED) {

                    // create a feature layer to display the shapefile
                    FeatureLayer shapefileFeatureLayer = new FeatureLayer(shapefileFeatureTable);

                    // add the feature layer to the map
                    mMapView.getMap().getOperationalLayers().add(shapefileFeatureLayer);

                    // zoom the map to the extent of the shapefile
                    mMapView.setViewpointAsync(new Viewpoint(shapefileFeatureLayer.getFullExtent()));
                } else {
                    String error = "Shapefile feature table failed to load: " + shapefileFeatureTable.getLoadError().toString();
                    Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
                    Log.e(TAG, error);
                }
            }
        });
    }

}
