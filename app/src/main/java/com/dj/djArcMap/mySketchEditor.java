package com.dj.djArcMap;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.example.administrator.mymap.R;

/**
 * Created by 杜杰 on 2018/4/20.
 */

public class mySketchEditor implements View.OnClickListener {
    private SketchEditor mSketchEditor;
    private View editeToolbar;
    private Button myToolbar;
    private Toolbar mToolbar;
    private GraphicsOverlay mGraphicsOverlay;
    private SimpleMarkerSymbol mPointSymbol;
    private SimpleLineSymbol mLineSymbol;
    private SimpleFillSymbol mFillSymbol;
    private ImageButton mPointButton;
    private ImageButton mMultiPointButton;
    private ImageButton mPolylineButton;
    private ImageButton mPolygonButton;
    private ImageButton mFreehandLineButton;
    private ImageButton mFreehandPolygonButton;

    public mySketchEditor(MapView mapView, View editeToolbar, Button myToolbar, Toolbar mToolbar){
        // create a new sketch editor and add it to the map view
        mSketchEditor = new SketchEditor();
        mapView.setSketchEditor(mSketchEditor);
        mGraphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(mGraphicsOverlay);
        this.editeToolbar = editeToolbar;
        this.myToolbar = myToolbar;
        this.mToolbar = mToolbar;

        mPointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 20);
        mLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFFFF8800, 4);
        mFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.CROSS, 0x40FFA9A9, mLineSymbol);
    }

    public void setButtons(ImageButton mPointButton, ImageButton mMultiPointButton, ImageButton mPolylineButton,
            ImageButton mPolygonButton, ImageButton mFreehandLineButton, ImageButton mFreehandPolygonButton){
        this.mPointButton = mPointButton;
        this.mMultiPointButton = mMultiPointButton;
        this.mPolylineButton = mPolylineButton;
        this.mPolygonButton = mPolygonButton;
        this.mFreehandLineButton = mFreehandLineButton;
        this.mFreehandPolygonButton = mFreehandPolygonButton;

        mPointButton.setOnClickListener(this);
        mMultiPointButton.setOnClickListener(this);
        mPolylineButton.setOnClickListener(this);
        mPolygonButton.setOnClickListener(this);
        mFreehandLineButton.setOnClickListener(this);
        mFreehandPolygonButton.setOnClickListener(this);
    }

    /**
     * 按钮点击事件的实现
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pointButton:
                createModePoint();
                break;
            case R.id.pointsButton:
                createModeMultipoint();
                break;
            case R.id.polylineButton:
                createModePolyline();
                break;
            case R.id.polygonButton:
                createModePolygon();
                break;
            case R.id.freehandLineButton:
                createModeFreehandLine();
                break;
            case R.id.freehandPolygonButton:
                createModeFreehandPolygon();
                break;
        }
    }

    /**
     * When the undo button is clicked, undo the last event on the SketchEditor.
     */
    public void undo() {
        if (mSketchEditor.canUndo()) {
            mSketchEditor.undo();
        }
    }

    /**
     * When the redo button is clicked, redo the last undone event on the SketchEditor.
     */
    public void redo() {
        if (mSketchEditor.canRedo()) {
            mSketchEditor.redo();
        }
    }

    /**
     * 显示或隐藏编辑工具栏
     */
    public void start(){
        if(editeToolbar.getVisibility()== View.GONE){
            editeToolbar.setVisibility(View.VISIBLE);
        }else{
            editeToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 显示或隐藏菜单栏
     */
    private void in_visible(){
        if(mToolbar.getVisibility()==View.GONE){
            mToolbar.setVisibility(View.VISIBLE);
        }else{
            mToolbar.setVisibility(View.GONE);
        }
    }
    /**
     * When the stop button is clicked, check that sketch is valid. If so, get the geometry from the sketch, set its
     * symbol and add it to the graphics overlay.
     */
    public void stop() {
        if (!mSketchEditor.isSketchValid()) {
            reportNotValid();
            mSketchEditor.stop();
            resetButtons();
            return;
        }

        // get the geometry from sketch editor
        Geometry sketchGeometry = mSketchEditor.getGeometry();
        mSketchEditor.stop();
        resetButtons();

        if (sketchGeometry != null) {

            // create a graphic from the sketch editor geometry
            Graphic graphic = new Graphic(sketchGeometry);

            // assign a symbol based on geometry type
            if (graphic.getGeometry().getGeometryType() == GeometryType.POLYGON) {
                graphic.setSymbol(mFillSymbol);
            } else if (graphic.getGeometry().getGeometryType() == GeometryType.POLYLINE) {
                graphic.setSymbol(mLineSymbol);
            } else if (graphic.getGeometry().getGeometryType() == GeometryType.POINT ||
                    graphic.getGeometry().getGeometryType() == GeometryType.MULTIPOINT) {
                graphic.setSymbol(mPointSymbol);
            }

            // add the graphic to the graphics overlay
            mGraphicsOverlay.getGraphics().add(graphic);
        }
    }

    /**
     * Called if sketch is invalid. Reports to user why the sketch was invalid.
     */
    public void reportNotValid() {
        String validIf;
        if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.POINT) {
            validIf = "Point only valid if it contains an x & y coordinate.";
        } else if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.MULTIPOINT) {
            validIf = "Multipoint only valid if it contains at least one vertex.";
        } else if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.POLYLINE
                || mSketchEditor.getSketchCreationMode() == SketchCreationMode.FREEHAND_LINE) {
            validIf = "Polyline only valid if it contains at least one part of 2 or more vertices.";
        } else if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.POLYGON
                || mSketchEditor.getSketchCreationMode() == SketchCreationMode.FREEHAND_POLYGON) {
            validIf = "Polygon only valid if it contains at least one part of 3 or more vertices which form a closed ring.";
        } else {
            validIf = "No sketch creation mode selected.";
        }
        String report = "Sketch geometry invalid:\n" + validIf;
        final Snackbar reportSnackbar = Snackbar.make(editeToolbar, report, Snackbar.LENGTH_INDEFINITE);
        reportSnackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportSnackbar.dismiss();
            }
        });
        TextView snackbarTextView = (TextView) reportSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarTextView.setSingleLine(false);
        reportSnackbar.show();
        Log.i("dj", report);
    }


    /**
     * When the point button is clicked, reset other buttons, show the point button as selected, and start point
     * drawing mode.
     */
    private void createModePoint() {
        resetButtons();
        mPointButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.POINT);
    }

    /**
     * When the multipoint button is clicked, reset other buttons, show the multipoint button as selected, and start
     * multipoint drawing mode.
     */
    private void createModeMultipoint() {
        resetButtons();
        mMultiPointButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.MULTIPOINT);
    }

    /**
     * When the polyline button is clicked, reset other buttons, show the polyline button as selected, and start
     * polyline drawing mode.
     */
    private void createModePolyline() {
        resetButtons();
        mPolylineButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.POLYLINE);

    }

    /**
     * When the polygon button is clicked, reset other buttons, show the polygon button as selected, and start polygon
     * drawing mode.
     */
    private void createModePolygon() {
        resetButtons();
        mPolygonButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.POLYGON);
    }

    /**
     * When the freehand line button is clicked, reset other buttons, show the freehand line button as selected, and
     * start freehand line drawing mode.
     */
    private void createModeFreehandLine() {
        resetButtons();
        mFreehandLineButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.FREEHAND_LINE);
    }

    /**
     * When the freehand polygon button is clicked, reset other buttons, show the freehand polygon button as selected,
     * and enable freehand polygon drawing mode.
     */
    private void createModeFreehandPolygon() {
        resetButtons();
        mFreehandPolygonButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
    }

    /**
     * De-selects all buttons.取消选中的按钮
     */
    public void resetButtons() {
        mPointButton.setSelected(false);
        mMultiPointButton.setSelected(false);
        mPolylineButton.setSelected(false);
        mPolygonButton.setSelected(false);
        mFreehandLineButton.setSelected(false);
        mFreehandPolygonButton.setSelected(false);
    }

}
