package com.example.administrator.mymap;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dj.tools.BaseActivity;

/**
 * 继承BaseActivity将创建的活动添加到BaseActivity下的一个List中，方便以后的管理
 */
public class DJLog extends BaseActivity {
    private String uname;
    private String upassword;
    private Button login;
    private Button register;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);
        login = (Button)findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        activity = this;
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        /**
         * 登陆按钮事件，跳转主页面
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = ((EditText)findViewById(R.id.uname)).getText().toString();
                upassword = ((EditText)findViewById(R.id.upassword)).getText().toString();
                //如果用户名和密码正确则跳转页面，否则弹出提示框
                if(uname.equals("dj")&&upassword.equals("123dj321")){
                    Intent intent = new Intent(activity, djtest.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(activity, "用户名或密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });

        /**
         * 注册按钮，跳转到注册页面中
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Register.class);
                startActivity(intent);
            }
        });


    }

}
