package com.dj.TianDiTu;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;

/**
 * Created by 杜杰 on 2018/1/4.
 */

public class TianDiTuInfo {
    private String url;
    private String layerName;

    private int minZoomLevel = 0;
    private int maxZoomLevel = 17;

    private int srid;

    private String tileMatrixSet;

    private Envelope envelope;
    private TileInfo tileInfo;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public int getMinZoomLevel() {
        return minZoomLevel;
    }

    public void setMinZoomLevel(int minZoomLevel) {
        this.minZoomLevel = minZoomLevel;
    }

    public int getMaxZoomLevel() {
        return maxZoomLevel;
    }

    public void setMaxZoomLevel(int maxZoomLevel) {
        this.maxZoomLevel = maxZoomLevel;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public String getTileMatrixSet() {
        return tileMatrixSet;
    }

    public void setTileMatrixSet(String tileMatrixSet) {
        this.tileMatrixSet = tileMatrixSet;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public TileInfo getTileInfo() {
        return tileInfo;
    }

    public void setTileInfo(TileInfo tileInfo) {
        this.tileInfo = tileInfo;
    }

}
