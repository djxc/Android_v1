package com.dj.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dj.tools.ActivityCollector;
import com.example.administrator.mymap.DJLog;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context, "这是自己创造的广播", Toast.LENGTH_LONG).show();
    }
}
