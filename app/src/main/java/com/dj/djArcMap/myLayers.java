package com.dj.djArcMap;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.raster.Raster;

import static com.dj.djArcMap.map1.TAG;

/**
 * Created by 杜杰 on 2018/4/25.
 */

public class myLayers {


    public myLayers(){

    }

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

    /**
     * Loads Shasta.tif as a Raster and adds it to a new RasterLayer. The RasterLayer is then added
     * to the map as an operational layer. Map viewpoint is then set based on the Raster's geometry.
     */
    public void loadRaster(final MapView mMapView, ArcGISMap map, String raster_path) {
        // create a raster from a local raster file
        Raster raster = new Raster( Environment.getExternalStorageDirectory() + "/" + raster_path);
        // create a raster layer
        final RasterLayer rasterLayer = new RasterLayer(raster);
        // add the raster as an operational layer
        map.getOperationalLayers().add(rasterLayer);
        // set viewpoint on the raster
        rasterLayer.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                mMapView.setViewpointGeometryAsync(rasterLayer.getFullExtent(), 50);
            }
        });
    }


}
