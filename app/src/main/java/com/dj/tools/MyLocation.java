package com.dj.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.dj.djArcMap.map1;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class MyLocation {
    private static final String TAG = "dj";
    private Activity activity;
    private LocationManager locationManager;
    private Location location;
    private String locationProvider;
    private List<GpsSatellite> numSatelliteList = new ArrayList<>(); // 卫星信号
    private EditText sate_num = null;
    private EditText location_info = null;
    private MapView mapView;
    private GraphicsOverlay LocationLayer = new GraphicsOverlay();
    private SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.TRIANGLE, Color.BLUE, 14);

    public MyLocation(Activity activity, MapView mapView){
        this.activity = activity;
        this.mapView = mapView;
        this.mapView.getGraphicsOverlays().add(LocationLayer);
        init_location();
    }

    public MyLocation(Activity activity, EditText sate_num, EditText location_info){
        this.activity = activity;
        this.sate_num = sate_num;
        this.location_info = location_info;
        init_location();
    }

    /**
     * 初始化位置相关的类，创建LocationManager，检查GPS是否打开，新建位置监听器
     */
    private void init_location(){
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;
        openGPSSettings();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: 没有权限 ");
            getPermission();
            return;
        }
        location = locationManager.getLastKnownLocation(locationProvider);      //获取最近一次可用的位置信息
        getLocationInfo();
        /**
         * LocationListern监听器
         * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
         */
        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLocationInfo();

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    // GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        Log.i(TAG, "当前GPS状态为可见状态");
                        break;
                    // GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.i(TAG, "当前GPS状态为服务区外状态");
                        break;
                    // GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.i(TAG, "当前GPS状态为暂停服务状态");
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
            }

        };

        locationManager.requestLocationUpdates(locationProvider, 1000, 10, ll);

        locationManager.addGpsStatusListener(statusListener);
    }

    /**
     * 申请权限
     */
    private void getPermission(){
        int fLocation = activity.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int cLocation = activity.checkCallingOrSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && fLocation!= PackageManager.PERMISSION_GRANTED
                && cLocation!= PackageManager.PERMISSION_GRANTED ) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
    }

    /**
     * 检查是否打开了GPS工具
     */
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return;
        }
        Toast.makeText(activity, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        activity.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
    }

    /**
     * 获取位置的详细信息
     */
    public void getLocationInfo(){
        StringBuilder locInfo = null;
        locInfo = new StringBuilder("位置信息：\n");
        Point point = null;
        double lat = 0.0, lng = 0.0;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            point = new Point(lng, lat, SpatialReferences.getWgs84());

            locInfo.append("纬度：" + lat + "\n经度：" + lng);

            if (location.hasAccuracy()) {
                locInfo.append("\n精度：" + location.getAccuracy());
            }

            if (location.hasAltitude()) {
                locInfo.append("\n海拔：" + location.getAltitude() + "m");
            }

            if (location.hasBearing()) {// 偏离正北方向的角度
                locInfo.append("\n方向：" + location.getBearing());
            }

            if (location.hasSpeed()) {
                if (location.getSpeed() * 3.6 < 5) {
                    locInfo.append("\n速度：0.0km/h");
                } else {
                    locInfo.append("\n速度：" + location.getSpeed() * 3.6 + "km/h");
                }
            }
        } else {
            locInfo.append("没有位置信息！");
        }
        if(mapView!=null&&point!=null){
            addPoint(point);
            Toast.makeText(activity, "纬度：" + lat + "\n经度：" + lng, Toast.LENGTH_LONG).show();
        }
        if(location_info!=null){
            location_info.setText(locInfo);
        }
    }

    /**
     * 位置移动之后就会添加一个点
     * @param point
     */
    public void addPoint(Point point){
        LocationLayer.getGraphics().add(new Graphic(point, markerSymbol));
    }
    /**
     * 卫星状态监听器
     */

    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onCreate: 没有权限 ");
                getPermission();
                return;
            }
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            GpsStatus status = locationManager.getGpsStatus(null); //取当前状态
            updateGpsStatus(event, status);
        }
    };

    /**
     *
     * @param event
     * @param status
     */
    private void updateGpsStatus(int event, GpsStatus status) {
        StringBuilder num = new StringBuilder("");
        if (status == null) {
            num.append("搜索到卫星个数：" +0);
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                numSatelliteList.add(s);
                count++;
            }
            num.append("搜索到卫星个数：" + numSatelliteList.size());
        }
        if(sate_num!=null){
            sate_num.setText(num);
        }
    }

}
