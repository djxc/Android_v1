package com.dj.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.mymap.DJLog;

/**
 * Created by Administrator on 2018/5/2.
 */

public class BaseActivity extends AppCompatActivity {
    private forceOfflineReceiver forceOffline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.dj.forceOffline");
        forceOffline = new forceOfflineReceiver();
        registerReceiver(forceOffline, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(forceOffline!=null){
            unregisterReceiver(forceOffline);
            forceOffline = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class forceOfflineReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("forceOffline");
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        ActivityCollector.finishAll();
                        Intent intent = new Intent(context, DJLog.class);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Log.i("dj",e.toString());
                    }
                }
            });
            builder.show();
        }
    }

}
