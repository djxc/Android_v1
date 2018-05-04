package com.example.administrator.mymap;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private Activity activity;
    final String Tag = "dj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        //获取上一个intent传递过来的数据
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Toast.makeText(activity, data, Toast.LENGTH_LONG).show();

        activity = this;
        mapView = (MapView) findViewById(R.id.mapView);
        ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 34.056295, -117.195800, 16);
        mapView.setMap(map);
    }

    /**
     * 添加菜单，在menu中定义的菜单添加到activity中
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /**
     * 对菜单每个选项选择的对应的事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(activity, "这是添加按钮", Toast.LENGTH_LONG).show();
                break;
            case R.id.remove_item:
                Toast.makeText(activity, "这是删除按钮", Toast.LENGTH_LONG).show();
                break;
            case R.id.close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        mapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.dispose();
    }

}
