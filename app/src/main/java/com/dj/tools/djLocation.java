package com.dj.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mymap.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class djLocation extends AppCompatActivity {

    private StringBuilder sb;
    private Activity activity;
    private static final String TAG = "dj";
    private LocationManager locationManager;
    private String locationProvider;
    private  Location location;
    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号
    private EditText num_sate;
    private EditText laction_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_location);
        activity = this;
        num_sate = (EditText) findViewById(R.id.sate_num);
        laction_info = (EditText) findViewById(R.id.location_info);
        new MyLocation(activity, num_sate, laction_info);
//        openGPSSettings();      //检查用户是否打开了GPS功能
//        //创建LocationManager对象，所有的GPS定位服务都由其对象产生并进行控制
//        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
//        locationProvider = LocationManager.GPS_PROVIDER;
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "onCreate: 没有权限 ");
//            getPermission();
//            return;
//        }
//
//        location = locationManager.getLastKnownLocation(locationProvider);      //获取最近一次可用的位置信息
//        updateMsg(location);
//
//        /**
//         * LocationListern监听器
//         * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
//         */
//        LocationListener ll = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                updateMsg(location);
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                switch (status) {
//                    // GPS状态为可见时
//                    case LocationProvider.AVAILABLE:
//                        Log.i(TAG, "当前GPS状态为可见状态");
//                        break;
//                    // GPS状态为服务区外时
//                    case LocationProvider.OUT_OF_SERVICE:
//                        Log.i(TAG, "当前GPS状态为服务区外状态");
//                        break;
//                    // GPS状态为暂停服务时
//                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                        Log.i(TAG, "当前GPS状态为暂停服务状态");
//                        break;
//                }
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
//            }
//
//        };
//
//        locationManager.requestLocationUpdates(locationProvider, 1000, 1, ll);
//
//        locationManager.addGpsStatusListener(statusListener);

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
     *
     * @param loc
     * @return
     */
    private void updateMsg(Location loc) {
        sb = null;
        sb = new StringBuilder("位置信息：\n");
        if (loc != null) {
            double lat = loc.getLatitude();
            double lng = loc.getLongitude();

            sb.append("纬度：" + lat + "\n经度：" + lng);

            if (loc.hasAccuracy()) {
                sb.append("\n精度：" + loc.getAccuracy());
            }

            if (loc.hasAltitude()) {
                sb.append("\n海拔：" + loc.getAltitude() + "m");
            }

            if (loc.hasBearing()) {// 偏离正北方向的角度
                sb.append("\n方向：" + loc.getBearing());
            }

            if (loc.hasSpeed()) {
                if (loc.getSpeed() * 3.6 < 5) {
                    sb.append("\n速度：0.0km/h");
                } else {
                    sb.append("\n速度：" + loc.getSpeed() * 3.6 + "km/h");
                }
            }
        } else {
            sb.append("没有位置信息！");
        }
        laction_info.setText(sb);
    }

    /**
     * 检查是否打开了GPS工具
     */
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            activity.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 卫星状态监听器
     */

    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        StringBuilder sb2 = new StringBuilder("");
        if (status == null) {
            sb2.append("搜索到卫星个数：" +0);
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
            sb2.append("搜索到卫星个数：" + numSatelliteList.size());
        }
        num_sate.setText(sb2);
    }

}
