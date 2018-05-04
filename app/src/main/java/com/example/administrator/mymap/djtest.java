package com.example.administrator.mymap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dj.Broadcast.NetworkChangeReceiver;
import com.dj.DataOperator.MyDatabaseHelper;
import com.dj.DataOperator.OperateFile;
import com.dj.DataOperator.SDCard;
import com.dj.DataOperator.dao;
import com.dj.ListViewTest;
import com.dj.djArcMap.map1;
import com.dj.djArcMap.myBaseMap;
import com.dj.myChat.ChatRoom;
import com.dj.myChat.Email_test;
import com.dj.myView.RecyclerViewTest;
import com.dj.tools.BaseActivity;

public class djtest extends BaseActivity implements View.OnClickListener{
    final String TAG = "dj";
    private Button openMap;
    private Button oBrowser;
    private Activity activity;
    private Button phone;
    private Button alertDialog;
    private Button djListView;
    private Button djRecyclerV;
    private Button btn_myChat;
    private Button btn_broadcast;
    private Button btn_myBroadcast;
    private Button btn_forceOffline;
    private Button btn_SDcard;
    private Button btn_readFile;
    private Button btn_createDB;
    private Button btn_query;
    private IntentFilter intentFilter;
    private dj networkChangeReceiver;
    private StorageManager mStorageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        mStorageManager = (StorageManager)getSystemService(Context.STORAGE_SERVICE);
        activity = this;
        openMap = (Button) findViewById(R.id.map);
        oBrowser = (Button) findViewById(R.id.browser);
        phone = (Button) findViewById(R.id.phone);
        alertDialog = (Button) findViewById(R.id.myDialog);
        djListView = (Button) findViewById(R.id.djListView);
        djRecyclerV = (Button) findViewById(R.id.djRecyclerView);
        btn_myChat = (Button) findViewById(R.id.btn_myChat);
        btn_broadcast = (Button) findViewById(R.id.btn_boadcast);
        btn_myBroadcast = (Button) findViewById(R.id.btn_myBoadcast);
        btn_forceOffline = (Button) findViewById(R.id.btn_forceOffline);
        btn_SDcard = (Button) findViewById(R.id.btn_getSDcard);
        btn_readFile = (Button) findViewById(R.id.btn_readFile);
        btn_createDB = (Button) findViewById(R.id.btn_createDB);
        btn_query = (Button) findViewById(R.id.btn_queryData);
        Button btn_email = (Button) findViewById(R.id.btn_email);

        btn_email.setOnClickListener(this);

        btn_query.setOnClickListener(this);

        btn_createDB.setOnClickListener(this);

        btn_readFile.setOnClickListener(this);

        btn_SDcard.setOnClickListener(this);

        btn_forceOffline.setOnClickListener(this);

        btn_myBroadcast.setOnClickListener(this);

        btn_broadcast.setOnClickListener(this);

        btn_myChat.setOnClickListener(this);

        djRecyclerV.setOnClickListener(this);

        djListView.setOnClickListener(this);
        /**
         * 打开对话框
         */
        alertDialog.setOnClickListener(this);

        /**
         * 打开地图,通过intent传递数据
         */
        openMap.setOnClickListener(this);

        /**
         * 打开浏览器
         */
        oBrowser.setOnClickListener(this);

        /**
         * 打电话
         */
        phone.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(networkChangeReceiver);
        }catch (Exception e){
            Log.i("dj", e.toString());
        }
    }

    /**
     * 实现view点击事件响应
     * @param v
     */
    @Override
    public void onClick(View v) {
        String SDpath = getDatapath();
        String dbpath = SDpath + "mydb.db";
        switch (v.getId()){
            case R.id.phone:
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:18366134175"));
                startActivity(call);
                break;
            case R.id.browser:
                Intent browser = new Intent(Intent.ACTION_VIEW);
                browser.setData(Uri.parse("http://www.baidu.com"));
                startActivity(browser);
                break;
            case R.id.map:
                String data = "GIS";
                Intent intent = new Intent(activity, map1.class);
                intent.putExtra("data", data);
                startActivity(intent);
                break;
            case R.id.myDialog:
               myDialog("测试弹出对话框", "请选择yes或是no");
                break;
            case R.id.djListView:
                jumpto(ListViewTest.class);
                break;
            case R.id.djRecyclerView:
                jumpto(RecyclerViewTest.class);
                break;
            case R.id.btn_myChat:
                jumpto(ChatRoom.class);
                break;
            case R.id.btn_boadcast:
                Create_NetReceiver();
                break;
            case R.id.btn_myBoadcast:
                Create_Broastcast("com.dj.xc");
                break;
            case R.id.btn_forceOffline:
                Create_Broastcast("com.dj.forceOffline");
                break;
            case R.id.btn_getSDcard:
                new OperateFile().write("djdjdjd", SDpath);
                break;
            case R.id.btn_readFile:
                new OperateFile().readFile(SDpath);
                break;
            case R.id.btn_createDB:
                new dao(activity, dbpath).insertData();
                break;
            case R.id.btn_queryData:
                new dao(activity, dbpath).queryData();
                break;
            case R.id.btn_email:
                jumpto(Email_test.class);
                break;
        }
    }

    /**
     * 获取手机内存的位置
     * @return
     */
    private String getDatapath(){
        return new SDCard(activity).getStorages(mStorageManager).get(0).toString() + "/djData/" ;
    }

    private void makeToast(String string){
        Toast.makeText(activity, string, Toast.LENGTH_LONG).show();
    }
    /**
     * 接收系统广播
     */
    private void Create_NetReceiver(){
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new dj();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    /**
     * 自定义创建广播、发送广播，根据传递的名称创建广播
     * @param broadcastName
     */
    private void Create_Broastcast(String broadcastName){
        Intent intent = new Intent(broadcastName);
        sendBroadcast(intent);
    }

    /**
     * 定义alertdialog
     * @param title
     * @param message
     */
    private void myDialog(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "你选择了yes", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "你选择了no", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    /**
     * 使用inent进行页面的跳转，传递活动的类参数
     * @param targetClass
     */
    private void jumpto(Class targetClass){
        Intent intent = new Intent(activity, targetClass);
        startActivity(intent);
    }

    /**
     * 创建广播接收器
     */
    class dj extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable()){
                Toast.makeText(context, "网络已连接", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context, "网络不可用", Toast.LENGTH_LONG).show();
            }
        }
    }

}
