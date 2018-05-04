package com.example.administrator.mymap;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dj.tools.ActivityCollector;

/**
 * Created by Administrator on 2018/4/30.
 */

public class TitleLayout extends LinearLayout {

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        Button back = (Button) findViewById(R.id.title_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               Activity activity = (Activity)getContext();
                activity.finish();
            }
        });
    }

}
